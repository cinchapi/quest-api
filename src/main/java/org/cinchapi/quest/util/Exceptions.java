package org.cinchapi.quest.util;

import com.google.common.base.Strings;

/**
 * Utility class for handling Exceptions.
 * 
 * @author jnelson
 */
public class Exceptions {

    /**
     * Reliably get a message that describes the {@code exception}.
     * 
     * @param e
     * @return the Exception message
     */
    public static String getMessage(Exception exception) {
        String message = exception.getMessage();
        if(Strings.isNullOrEmpty(message)) {
            message = exception.getClass().getSimpleName();
        }
        return message;
    }

}
