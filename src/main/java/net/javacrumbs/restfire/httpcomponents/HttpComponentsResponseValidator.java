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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

/**
 * Validates the response.
 */
public class HttpComponentsResponseValidator implements ResponseValidator {
    private final HttpResponse response;
    private final byte[] responseBody;
    private final Charset charset;
    private final int duration;

    public HttpComponentsResponseValidator(HttpClient httpClient, HttpRequestBase method) {
        try {
            long executionStart = System.currentTimeMillis();
            this.response = httpClient.execute(method);
            //let's hope that the request took less than 4 years. This will overflow otherwise.
            duration = (int)(System.currentTimeMillis() - executionStart);

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity !=null) {
                responseBody = EntityUtils.toByteArray(responseEntity);
                charset = getCharset(responseEntity);
            } else {
                responseBody = null;
                charset = null;
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
        MatcherAssert.assertThat("Expected different body", new String(responseBody, charset), bodyMatcher);
        return this;
    }

    public ResponseValidator havingRawBody(Matcher<byte[]> bodyMatcher) {
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

    public ResponseValidator havingResponseTimeInMillis(Matcher<Integer> matcher) {
        MatcherAssert.assertThat("Unexpected response time", duration, matcher);
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

    private Charset getCharset(HttpEntity entity) {
        Charset charset = null;
        ContentType contentType = ContentType.get(entity);
        if (contentType != null) {
            charset = contentType.getCharset();
        }
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET;
        }
        return charset;
    }
}
