/**
 * Copyright 2009-2013 the original author or authors.
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

import org.hamcrest.Matcher;

/**
 * Validates the response
 */
public interface ResponseValidator {
    /**
     * Checks the response status.
     * @param status
     * @return
     */
    ResponseValidator havingStatusEqualTo(int status);

    /**
     * Checks the response status.
     * @param statusMatcher
     * @return
     */
    ResponseValidator havingStatus(Matcher<? super Integer> statusMatcher);

    /**
     * Compares response body
     * @param body
     * @return
     */
    ResponseValidator havingBodyEqualTo(String body);

    /**
     * Compares response body.
     * @param bodyMatcher
     * @return
     */
    ResponseValidator havingBody(Matcher<? super String> bodyMatcher);

    /**
     * Compares response body as byte array
     * @return
     */
    ResponseValidator havingRawBody(Matcher<? super byte[]> bodyMatcher);

    /**
     * Checks if there is a header with given value.
     * @param name
     * @param value
     * @return
     */
    ResponseValidator havingHeaderEqualTo(String name, String value);

    /**
     * Checks response headers.
     * @param name
     * @param matcher
     * @return
     */
    ResponseValidator havingHeader(final String name, final Matcher<? extends Iterable<? super String>> matcher);

    /**
     * Checks response time.
     */
    ResponseValidator havingResponseTimeInMillis(final Matcher<Long> matcher);
}
