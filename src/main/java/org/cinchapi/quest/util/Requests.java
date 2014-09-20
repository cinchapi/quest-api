package org.cinchapi.quest.util;

import java.net.InetAddress;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;

import spark.Request;

/**
 * Utilities related to http requests.
 * 
 * @author jnelson
 */
public final class Requests {

    /**
     * Return the client IP address, making a best effort to determine the
     * original IP if the request has been proxied.
     * 
     * @param request - the client's request
     * @return the client IP address
     */
    public static String getClientIpAddress(Request request) {
        String ip = request.ip();
        try {
            InetAddress address = InetAddress.getByName(ip);
            if(address.isAnyLocalAddress() || address.isLoopbackAddress()) {
                ip = !Strings.isNullOrEmpty(request.headers("X-Forwarded-For")) ? request
                        .headers("X-Forwarded-For") : ip;
            }
        }
        catch (Exception e) {/* noop */}

        return ip;
    }

    /**
     * Return the client user-agent, making a best effort to check any relevant
     * headers for the information.
     * 
     * @param request - the client's request
     * @return the client user agent
     */
    public static String getClientUserAgent(Request request) {
        String userAgent = request.headers("User-Agent");
        if(Strings.isNullOrEmpty(userAgent)) {
            userAgent = "idk";
        }
        return userAgent;
    }

    /**
     * Return an MD5 hash that represents the fingerprint (ip address and
     * browser information) for the client.
     * 
     * @param request
     * @return the client fingerprint
     */
    public static String getClientFingerprint(Request request) {
        StringBuilder sb = new StringBuilder();
        sb.append(getClientUserAgent(request));
        sb.append(getClientIpAddress(request));
        return Hashing.md5().hashUnencodedChars(sb).toString();
    }

    private Requests() {/* noop */}

}
