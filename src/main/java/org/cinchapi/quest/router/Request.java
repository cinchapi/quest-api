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

import org.cinchapi.quest.util.Exceptions;

import spark.HaltException;
import spark.Response;
import spark.Route;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A {@link Request} is processed by a Controller in order to return a JSON
 * payload. In particular, this class takes care of some scaffolding,
 * error handling, etc.
 * <p>
 * Each {@link Request} will return a JSON response with the following members:
 * <ul>
 * <li>status - success or failed</li>
 * <li>payload - the relevant data returned from the request or an error message
 * </li>
 * </ul>
 * </p>
 * 
 * @author jnelson
 */
public abstract class Request extends Route implements Rewritable {

    /**
     * Check to ensure that any of the specified required {@link params} is
     * not {@code null} or an empty string. If so, halt the request
     * immediately.
     * 
     * @param params
     */
    protected static final void require(Object... params) {
        for (Object param : params) {
            if(param == null
                    || (param instanceof String && Strings
                            .isNullOrEmpty((String) param))) {
                halt(400, "Request is missing a required parameter");
            }
        }
    }

    /**
     * A message indicating the request succeeded.
     */
    private static String STATUS_SUCCESS = "success";

    /**
     * A message indicating that the request failed.
     */
    private static String STATUS_FAILED = "failed";

    /**
     * The associated request.
     */
    private spark.Request request;

    /**
     * The associated response.
     */
    private Response response;

    private String uri;

    /**
     * Construct a new instance.
     * 
     * @param path
     */
    protected Request(String path) {
        super(path);
        this.uri = path;
    }

    public final String getUri() {
        return uri;
    }

    @Override
    public final Object handle(spark.Request request, spark.Response response) {
        this.request = request;
        this.response = response;
        JsonObject json = new JsonObject();
        try {
            json.addProperty("status", STATUS_SUCCESS);
            json.add("payload", handle());
        }
        catch (HaltException e) {
            throw e;
        }
        catch (Exception e) {

            json.addProperty("status", STATUS_FAILED);
            json.addProperty("payload", Exceptions.getMessage(e));
        }
        this.response.type("application/json");
        return json;
    }

    /**
     * Return a parameter associated with the request being processed.
     * <p>
     * Prepend the name of the parameter with ":" if it is a variable in the
     * route (i.e. /foo/:id). Otherwise, it is assumed to be a query param (i.e.
     * /foo?id=).
     * </p>
     * 
     * @param param
     * @return the value associated with the param
     */
    protected String get(String param) {
        return param.startsWith(":") ? request.params(param) : request
                .queryParams(param);
    }

    /**
     * TODO
     * 
     * @param param
     * @return
     */
    protected String[] getAll(String param) {
        try {
            return request.queryMap(param).values();
        }
        catch (NullPointerException e) { // the param is not in the map, so
                                         // return an empty array
            return new String[] {};
        }
    }

    /**
     * Do the work to handle the request and return a {@link JsonElement}
     * payload.
     * 
     * @return the payload
     */
    protected abstract JsonElement handle();

}
