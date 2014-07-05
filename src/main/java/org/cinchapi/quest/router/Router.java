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

import spark.Spark;

/**
 * A {@link Router} is responsible for defining accessible routes and processing
 * a {@link AbstractView} or {@link Endpoint}.
 * 
 * @author jnelson
 */
public abstract class Router {

    /**
     * Define and implement the routes that are handled by this {@link Router}.
     */
    public abstract void routes();

    /**
     * Perform a GET request and process the {@link AbstractView}.
     * 
     * @param view
     */
    public void get(final RewritableRoute route) {
        route.prepend(getNamespace());
        Spark.get(route);
    }

    /**
     * Perform a POST request and process the {@link AbstractView}.
     * 
     * @param view
     */
    public void post(final RewritableRoute route) {
        route.prepend(getNamespace());
        Spark.post(route);
    }
 
    /**
     * Return the namespace for this particular router. This will be the name of
     * the class without the word "Router".
     * 
     * @return the namespace
     */
    private String getNamespace() {
        String namespace = this.getClass().getSimpleName()
                .replace("Router", "");
        namespace = namespace.replace("Index", "");
        namespace = namespace.toLowerCase();
        return namespace;
    }

}
