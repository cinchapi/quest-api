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
package org.cinchapi.quest.util;

import java.net.URL;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * URL based utility functions.
 * 
 * @author jnelson
 */
public final class URLs {

    /**
     * Cache of URL reachability states that is GC-aware.
     */
    private static final LoadingCache<String, Boolean> cache = CacheBuilder
            .newBuilder().weakKeys().softValues()
            .build(new CacheLoader<String, Boolean>() {

                @Override
                public Boolean load(String key) throws Exception {
                    try {
                        new URL(key).openStream();
                        return true;
                    }
                    catch (Exception e) {
                        return false;
                    }
                }

            });

    /**
     * Return {@code true} if the {@code url} is reachable.
     * 
     * @param url
     * @return {@code true} if the {@code url} can be reached.
     */
    public static boolean isReachable(String url) {
        try {
            return cache.get(url);
        }
        catch (ExecutionException e) {
            throw Throwables.propagate(e);
        }

    }

    private URLs() {/* noop */}

}
