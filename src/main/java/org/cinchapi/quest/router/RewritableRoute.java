/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Jeff Nelson, Cinchapi Software Collective
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cinchapi.quest.router;

import java.lang.reflect.Field;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import spark.Response;
import spark.Request;
import spark.Route;
import spark.template.mustache.MustacheTemplateRoute;

/**
 * A {@link RewritableRoute} is one that can have its path altered in certain
 * circumstances. This base class provides some methods to do re-writing in a
 * convenient way.
 * <p>
 * <em>Rather than extending this class directly, consider using
 * {@link AbstractRewritableRoute} as a parent class since it takes care of some
 * boilerplate scaffolding and provides some helpful utility functions.</em>
 * </p>
 * 
 * @author jnelson
 */
// NOTE: We are extending the MustacheTemplateRoute this high up in the chain so
// that View subclasses can access the necessary methods while also benefiting
// from some of the non-view scaffolding that happens in this and other bases
// classes.
public abstract class RewritableRoute extends MustacheTemplateRoute {

    /**
     * The {@link Request} object that is associated with this {@link Route}.
     * While this object is accessible to subclasses, caution should be
     * exercised when operating on this object directly.
     */
    protected Request request;

    /**
     * The {@link Response} object that is associated with this {@link Route}.
     * While this object is accessible to subclasses, caution should be
     * exercised when operating on this object directly.
     */
    protected Response response;

    /**
     * Construct a new instance.
     * 
     * @param relativePath
     */
    protected RewritableRoute(String relativePath) {
        super(relativePath);
    }

    /**
     * Get the path that describes this route.
     * 
     * @return the path
     */
    protected String getRoutePath() {
        try {
            Class<?> parent = this.getClass().getSuperclass();
            Field field = null;
            while (field == null && parent != Object.class) {
                try {
                    field = parent.getDeclaredField("path");
                }
                catch (NoSuchFieldException e) {
                    parent = parent.getSuperclass();
                }
            }
            Preconditions.checkState(field != null);
            field.setAccessible(true);
            return (String) field.get(this);
        }
        catch (ReflectiveOperationException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * Rewrite this {@link Route} by prepending the {@code namespace} to the
     * relative path that was specified in the constructor.
     * 
     * @param namespace
     */
    protected void prepend(String namespace) {
        if(!Strings.isNullOrEmpty(namespace)
                && !namespace.equalsIgnoreCase("index")) {
            namespace = namespace.toLowerCase();
            try {
                Class<?> parent = this.getClass().getSuperclass();
                Field field = null;
                while (field == null && parent != Object.class) {
                    try {
                        field = parent.getDeclaredField("path");
                    }
                    catch (NoSuchFieldException e) {
                        parent = parent.getSuperclass();
                    }
                }
                Preconditions.checkState(field != null);
                field.setAccessible(true);
                String path = (String) field.get(this);
                path = path.startsWith("/") ? path : "/" + path;
                path = namespace + path;
                field.set(this, path);
            }
            catch (ReflectiveOperationException e) {
                throw Throwables.propagate(e);
            }
        }

    }
}
