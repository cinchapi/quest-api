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
 * A {@link Router} is responsible for defining accessible routes and serving
 * an {@link AbstractView} or {@link Endpoint}.
 * 
 * @author jnelson
 */
public abstract class Router {

    /**
     * The namespace is the name of the class without the word "Router". The
     * namespace is used to handle the appropriate re-writing for all the routes
     * that are served by this {@link Router}.
     */
    private final String namespace = this.getClass().getSimpleName()
            .replace("Router", "").replace("Index", "").toLowerCase();

    /**
     * Define and implement the routes that are handled by this {@link Router}.
     * Each route must respond to one of the HTTP verbs (GET, POST, PUT, DELETE)
     * and server either a {@link View} or {@link Endpoint}.
     * <p>
     * You may define multiple routes that process the same path as long as each
     * route responds to a different HTTP verb (i.e. you may have GET
     * /path/to/foo and POST /path/to/foo). On the other hand, you may not
     * define two routes that respond to the same HTTP Verb, even if they serve
     * different kinds of resources (i.e. you cannot have GET /path/to/foo that
     * serves a View and GET /path/to/foo that serves an Endpoint).
     * </p>
     * <p>
     * <h2>Defining Views</h2>
     * A {@link View} specifies the template to serve to the client and the data
     * to supply. Views are defined as follows:
     * 
     * <pre>
     * get(new View(&quot;/path/to/foo&quot;) {
     * 
     *     protected String template() {
     *         return &quot;foo.mustache&quot;;
     *     }
     * 
     *     protected Map&lt;String, Object&gt; data() {
     *         Map&lt;String, Object&gt; data = Maps.newHashMap();
     *         // populate data
     *         return data;
     *     }
     * 
     * });
     * </pre>
     * 
     * The router will fill in variables defined in the specified template with
     * the appropriate values from {@code data()}.
     * <p>
     * <p>
     * <h3>Defining Endpoints</h2> An {@link Endpoint} returns a json payload in
     * response to an HTTP request. Endpoints are defined as follows:
     * <pre>
     * </pre>
     * </p>
     */
    public abstract void routes();

    /**
     * Perform a GET request and process the {@link RewritableRoute}.
     * 
     * @param route
     */
    public void get(final RewritableRoute route) {
        route.prepend(namespace);
        Spark.get(route);
    }

    /**
     * Perform a POST request and process the {@link RewritableRoute}.
     * 
     * @param route
     */
    public void post(final RewritableRoute route) {
        route.prepend(namespace);
        Spark.post(route);
    }

    /**
     * Perform a PUT request and process the {@link RewritableRoute}.
     * 
     * @param route
     */
    public void put(final RewritableRoute route) {
        route.prepend(namespace);
        Spark.put(route);
    }

    /**
     * Perform a DELETE request and process the {@link RewritableRoute}.
     * 
     * @param route
     */
    public void delete(final RewritableRoute route) {
        route.prepend(namespace);
        Spark.delete(route);
    }

}
