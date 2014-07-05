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
package org.cinchapi.quest.app;

import static spark.Spark.setPort;
import static spark.Spark.staticFileLocation;

import java.util.Set;

import org.cinchapi.quest.router.Router;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

/**
 * The {@link AppRunner} looks for and registers all the route definitions.
 * 
 * @author jnelson
 */
public class AppRunner {

    // Controller configuration
    static {
        ((ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.ERROR);
        setPort(8090);
        staticFileLocation("public");
    }

    /**
     * Run the application...
     * 
     * @param args
     */
    public static void main(String... args) {
        Reflections.log = null; // turn off logging
        Reflections reflections = new Reflections();
        Set<Class<? extends Router>> routers = reflections
                .getSubTypesOf(Router.class);
        for (Class<? extends Router> router : routers) {
            try {
                System.out.println("Registering routes from " + router);
                router.newInstance().routes();
            }
            catch (Exception e) {
                continue;
            }
        }
    }

}
