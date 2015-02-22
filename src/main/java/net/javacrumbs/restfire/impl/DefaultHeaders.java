/**
 * Copyright 2009-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.restfire.impl;

import com.sun.net.httpserver.Headers;
import net.javacrumbs.restfire.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default header implementation.
 * We keep header name in separate collection since we want do not want to change case of
 * the names. In the map, we keep the names in lower-case.
 */
public class DefaultHeaders implements Response.Headers {
    private Headers headers = new Headers();
    private Map<String, List<String>> caseSensitiveHeaders = new HashMap<String, List<String>>();

    /**
     * Adds and normalizes the header.
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        headers.add(name, value);
        List<String> list = caseSensitiveHeaders.get(name);
        if (list == null) {
            list = new ArrayList<String>();
            caseSensitiveHeaders.put(name, list);
        }
        list.add(value);
    }

    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    public List<String> getHeaders(String name) {
        return postProcessList(headers.get(name));
    }

    private List<String> postProcessList(List<String> values) {
        return values != null ? Collections.unmodifiableList(values) : Collections.<String>emptyList();
    }

    public List<String> getHeadersCaseSensitive(String name) {
        return postProcessList(caseSensitiveHeaders.get(name));
    }

    public Collection<String> getHeaderNamesCaseSensitive() {
        return caseSensitiveHeaders.keySet();
    }
}
