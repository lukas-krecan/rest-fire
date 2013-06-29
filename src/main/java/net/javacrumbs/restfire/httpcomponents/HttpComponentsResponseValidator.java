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
package net.javacrumbs.restfire.httpcomponents;

import net.javacrumbs.restfire.ResponseValidator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;

public class HttpComponentsResponseValidator implements ResponseValidator {
    private final HttpResponse response;
    private final String responseBody;

    public HttpComponentsResponseValidator(HttpClient httpClient, HttpRequestBase method) {
        try {
            this.response = httpClient.execute(method);
            responseBody = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new IllegalArgumentException("Can not execute method", e);
        } finally {
            method.releaseConnection();
        }
    }

    public ResponseValidator havingStatusEqualTo(int status) {
        havingStatus(is(status));
        return this;
    }

    public ResponseValidator havingStatus(Matcher<Integer> statusMatcher) {
        MatcherAssert.assertThat("Expected different status code", response.getStatusLine().getStatusCode(), statusMatcher);
        return this;
    }

    public ResponseValidator havingBodyEqualTo(CharSequence body) {
        havingBody(is(body));
        return this;
    }

    public ResponseValidator havingBody(Matcher<? super CharSequence> bodyMatcher) {
        MatcherAssert.assertThat("Expected different body", responseBody, bodyMatcher);
        return this;
    }
}
