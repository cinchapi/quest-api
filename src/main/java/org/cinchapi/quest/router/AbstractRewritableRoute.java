package org.cinchapi.quest.router;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.cinchapi.quest.util.Objects;

import com.google.common.collect.Lists;

import spark.Request;
import spark.Response;

/**
 * The base class for routes that desire abstract request/response boilerplate
 * and scaffolding.
 * <p>
 * This class provides some utility functions around some of the native route
 * components with a cleaner interface.
 * </p>
 * <h2>Preconditions</h2>
 * <ul>
 * <li>Use the {@link #require(Object...)} method to ensure that the necessary
 * variables are all non-empty before continuing in the route, halting if the
 * check fails.</li>
 * </ul>
 * <h2>Redirection</h2>
 * <ul>
 * <li>Use the {@link Response#redirect(String) response.redirect(String)}
 * method to trigger a browser redirect to another location</li>
 * </ul>
 * 
 * @author jnelson
 */
public abstract class AbstractRewritableRoute extends RewritableRoute {

    /**
     * A collection that only contains an empty string, which is used to filter
     * lists (i.e. {@link List#removeAll(java.util.Collection)} empty strings).
     */
    private static List<String> EMPTY_STRING_COLLECTION = Lists
            .newArrayList("");

    /**
     * Check to ensure that none of the specified {@link params} is {@code null}
     * or an empty string or an empty collection. If so, halt
     * the request immediately.
     * 
     * @param params
     */
    protected static final void require(Object... params) {
        for (Object param : params) {
            if(Objects.isNullOrEmpty(param)) {
                halt(400, "Request is missing a required parameter");
            }
        }
    }

    /**
     * Construct a new instance.
     * 
     * @param relativePath
     */
    protected AbstractRewritableRoute(String relativePath) {
        super(relativePath);
    }

    /**
     * Return the list of values mapped from a parameter associated with the
     * request being processed. This method is only appropriate for query
     * params and will not work for route variables (because those cannot
     * contain multiple values).
     * <p>
     * <strong>NOTE:</strong> If there are no values for {@code param}, then an
     * empty list is returned.
     * </p>
     * 
     * @param param
     * @return the (possibly empty) list of values
     */
    protected List<String> getParamValues(String param) {
        try {
            List<String> list = Lists.newArrayList(request.queryMap(param)
                    .values());
            list.removeAll(EMPTY_STRING_COLLECTION);
            return list;
        }
        catch (NullPointerException e) { // the param is not in the map, so
                                         // return an empty array
            return Collections.unmodifiableList(EMPTY_STRING_COLLECTION);
        }
    }

    /**
     * Return a parameter associated with the request being processed.
     * <p>
     * Prepend the name of the parameter with {@code ":"} if it is a variable in
     * the route (i.e. /foo/:id). Otherwise, if the name of the parameter does
     * not start with {@code ":"} then it is assumed to be a variable in the
     * query string. (i.e. /foo?id=).
     * </p>
     * 
     * @param param
     * @return the value associated with the param or {@code null} if it is not
     *         provided or not found
     */
    @Nullable
    protected final String getParamValue(String param) {
        return param.startsWith(":") ? request.params(param) : request
                .queryParams(param);
    }

    /**
     * Handle the request that has been made to the path that corresponds to
     * this {@link Route}.
     * 
     * @param request
     * @param response
     * @return the content to be set in the response
     */
    protected abstract Object handle();

    @Override
    public final Object handle(Request request, Response response) {
        this.request = request;
        this.response = response;
        return handle();
    }

}
