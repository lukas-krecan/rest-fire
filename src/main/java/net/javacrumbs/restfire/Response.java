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
package net.javacrumbs.restfire;

import java.util.Collection;
import java.util.List;

/**
 * Response wrapper
 */
public interface Response {
    /**
     * HTTP response status.
     *
     * @return
     */
    public int getStatus();

    /**
     * HTTP response headers
     *
     * @return
     */
    public Headers getHeaders();

    /**
     * Returns value of a header. If header is not present, returns null, if there is multiple header values,
     * throws AssertionError.
     * @param headerName
     * @return
     */
    public String getHeader(String headerName);

    /**
     * Body as String decoded using response encoding
     *
     * @return
     */
    public String getBody();

    /**
     * Row bytes from body.
     *
     * @return
     */
    public byte[] getRawBody();

    /**
     * Duration in millis.
     */
    public long getDuration();

    /**
     * Returns response validator.
     *
     * @return
     */
    public ResponseValidator getValidator();

    /**
     * Fluent synonym thet returns validator.
     */
    public ResponseValidator is();


    /**
     * Response headers wrapper.
     */
    public static interface Headers {
        /**
         * Returns header names or an empty collection.
         *
         * @return
         */
        public Collection<String> getHeaderNames();

        /**
         * Returns header names or an empty collection.
         * The names case is not changed by the library.
         *
         * @return
         */
        public Collection<String> getHeaderNamesCaseSensitive();

        /**
         * Returns header values for given header or an empty collection.
         * Case-insensitive regarding the header name.
         *
         * @param name
         * @return
         */
        public List<String> getHeaders(String name);

        /**
         * Returns header values for given header or an empty collection.
         * Case-sensitive regarding the header name.
         *
         * @param name
         * @return
         */
        public List<String> getHeadersCaseSensitive(String name);
    }
}
