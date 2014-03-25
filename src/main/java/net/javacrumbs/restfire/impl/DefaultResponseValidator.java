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
package net.javacrumbs.restfire.impl;

import net.javacrumbs.restfire.Response;
import net.javacrumbs.restfire.ResponseValidator;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

/**
 * Validates the response.
 */
public class DefaultResponseValidator implements ResponseValidator {
    private final Response response;

    public DefaultResponseValidator(Response response) {
        this.response = response;
    }

    public ResponseValidator havingStatusEqualTo(int status) {
        havingStatus(is(status));
        return this;
    }

    public ResponseValidator havingStatus(Matcher<? super Integer> statusMatcher) {
        MatcherAssert.assertThat("Expected different status code", response.getStatus(), statusMatcher);
        return this;
    }

    public ResponseValidator havingBodyEqualTo(String body) {
        havingBody(is(body));
        return this;
    }

    public ResponseValidator havingBody(Matcher<? super String> bodyMatcher) {
        MatcherAssert.assertThat("Expected different body", response.getBody(), bodyMatcher);
        return this;
    }

    public ResponseValidator havingRawBody(Matcher<? super byte[]> bodyMatcher) {
        MatcherAssert.assertThat("Expected different body", response.getRawBody(), bodyMatcher);
        return this;
    }

    public ResponseValidator havingHeaderEqualTo(String name, String value) {
        havingHeader(name, hasItem(value));
        return this;
    }

    public ResponseValidator havingHeader(final String name, final Matcher<? super List<String>> headerMatcher){
        List<String> headers = response.getHeaders().getHeaders(name.toLowerCase());
        MatcherAssert.assertThat("Expected different header '" + name + "'", headers, headerMatcher);
        return this;
    }

    public ResponseValidator havingResponseTimeInMillis(Matcher<Long> matcher) {
        MatcherAssert.assertThat("Unexpected response time", response.getDuration(), matcher);
        return this;
    }

    /**
     * Returns response.
     *
     * @return
     */
    protected Response getResponse() {
        return response;
    }
}
