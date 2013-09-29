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
public class HttpComponentsResponseValidator<V extends ResponseValidator<V>> implements ResponseValidator<V> {
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

    public V havingStatusEqualTo(int status) {
        havingStatus(is(status));
        return (V)this;
    }

    public V havingStatus(Matcher<Integer> statusMatcher) {
        MatcherAssert.assertThat("Expected different status code", response.getStatusLine().getStatusCode(), statusMatcher);
        return (V)this;
    }

    public V havingBodyEqualTo(String body) {
        havingBody(is(body));
        return (V)this;
    }

    public V havingBody(Matcher<String> bodyMatcher) {
        String response = responseBody != null ? new String(responseBody, charset) : "";
        MatcherAssert.assertThat("Expected different body", response, bodyMatcher);
        return (V)this;
    }

    public V havingRawBody(Matcher<byte[]> bodyMatcher) {
        MatcherAssert.assertThat("Expected different body", responseBody, bodyMatcher);
        return (V)this;
    }

    public V havingHeaderEqualTo(String name, String value) {
        havingHeader(name, hasItem(value));
        return (V)this;
    }

    public V havingHeader(String name, Matcher<? super List<String>> headerMatcher) {
        MatcherAssert.assertThat("Expected different header '"+name+"'", getHeaderValues(name), headerMatcher);
        return (V)this;
    }

    public V havingResponseTimeInMillis(Matcher<Integer> matcher) {
        MatcherAssert.assertThat("Unexpected response time", duration, matcher);
        return (V)this;
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

    private static Charset getCharset(HttpEntity entity) {
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

    protected HttpResponse getResponse() {
        return response;
    }
}
