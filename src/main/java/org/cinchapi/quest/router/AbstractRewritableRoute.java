package org.cinchapi.quest.router;

import java.util.List;

import org.cinchapi.quest.util.Objects;

import com.google.common.collect.Lists;

import spark.Request;
import spark.Response;

/**
 * The base class for routes that desire abstract request/response boilerplate
 * and scaffolding.
 * 
 * @author jeffnelson
 * 
 */
public abstract class AbstractRewritableRoute extends RewritableRoute {

    /**
     * A collection that only contains an empty string, which is used to filter
     * lists.
     */
    private static List<String> EMPTY_STRING_COLLECTION = Lists
            .newArrayList("");

    /**
     * Check to ensure that any of the specified required {@link params} is
     * not {@code null} or an empty string or an empty collection. If so, halt
     * the request
     * immediately.
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
     * params.
     * 
     * @param param
     * @return the array of values
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
            return Lists.newArrayList();
        }
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
    protected final String getParamValue(String param) {
        return param.startsWith(":") ? request.params(param) : request
                .queryParams(param);
    }

    /**
     * Redirect the browse to {@code path}. This method always returns
     * {@code NULL}.
     * 
     * @param path
     * @return {@code NULL}
     */
    protected <T> T redirect(String path) {
        response.redirect(path);
        return null;
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
