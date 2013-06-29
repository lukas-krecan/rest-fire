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
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

/**
 * Validates the response.
 */
public class HttpComponentsResponseValidator implements ResponseValidator {
    private final HttpResponse response;
    private final String responseBody;

    public HttpComponentsResponseValidator(HttpClient httpClient, HttpRequestBase method) {
        try {
            this.response = httpClient.execute(method);
            if (response.getEntity()!=null) {
                responseBody = EntityUtils.toString(response.getEntity());
            } else {
                responseBody = null;
            }
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

    public ResponseValidator havingBodyEqualTo(String body) {
        havingBody(is(body));
        return this;
    }

    public ResponseValidator havingBody(Matcher<String> bodyMatcher) {
        MatcherAssert.assertThat("Expected different body", responseBody, bodyMatcher);
        return this;
    }

    public ResponseValidator havingHeaderEqualTo(String name, String value) {
        havingHeader(name, hasItem(value));
        return this;
    }

    public ResponseValidator havingHeader(String name, Matcher<? super List<String>> headerMatcher) {
        MatcherAssert.assertThat("Expected different header '"+name+"'", getHeaderValues(name), headerMatcher);
        return this;
    }

    private List<String> getHeaderValues(String name) {
        Header[] headers = response.getHeaders(name);
        if (headers.length == 0) {
            return null;
        }
        List<String> headerValues = new ArrayList<String>(headers.length);
        for (Header header : headers) {
            headerValues.add(header.getValue());
        }
        return headerValues;
    }
}
