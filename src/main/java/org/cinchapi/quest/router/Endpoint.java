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

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * An {@link Endpoint} is processed by a {@link Router} in order to return a
 * JSON
 * payload. In particular, this class takes care of some scaffolding,
 * error handling, etc.
 * <p>
 * Each {@link Endpoint} will return a JSON response with the following members:
 * <ul>
 * <li>status - success or failed</li>
 * <li>payload - the relevant data returned from the request or an error message
 * </li>
 * </ul>
 * </p>
 * 
 * @author jnelson
 */
public abstract class Endpoint extends AbstractRewritableRoute {

    /**
     * A {@link JsonElement} that represents the lack of any data being
     * returned.
     */
    protected static JsonNull NO_DATA = JsonNull.INSTANCE;

    /**
     * A message indicating the request succeeded.
     */
    private static String STATUS_SUCCESS = "success";

    /**
     * A message indicating that the request failed.
     */
    private static String STATUS_FAILED = "failed";

    /**
     * Construct a new instance.
     * 
     * @param path
     */
    public Endpoint(String path) {
        super(path);
    }

    @Override
    public final Object handle() {
        JsonObject json = new JsonObject();
        try {
            json.addProperty("status", STATUS_SUCCESS);
            json.add("payload", serve());
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
     * Serve the request with a {@link JsonElement} payload.
     * <p>
     * If this method returns, then the Router will assume that the request was
     * successful. If, for any reason, an error occurs, this method should throw
     * an Exception and the Router will wrap that in the appropriate response to
     * the caller.
     * </p>
     * 
     * @return the payload
     * @throws Exception
     */
    protected abstract JsonElement serve() throws Exception;

}
