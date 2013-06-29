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

import net.javacrumbs.restfire.PostRequestBuilder;
import net.javacrumbs.restfire.RequestBuilder;
import net.javacrumbs.restfire.ResponseValidator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public class HttpComponentsRequestBuilder implements PostRequestBuilder {
    private final HttpClient httpClient;
    private final HttpRequestBase method;

    public HttpComponentsRequestBuilder(HttpClient httpClient, HttpRequestBase method) {
        this.httpClient = httpClient;
        this.method = method;
    }

    public RequestBuilder withBody(String body) {
        if (method instanceof HttpEntityEnclosingRequestBase) {
            try {
                ((HttpEntityEnclosingRequestBase) method).setEntity(new StringEntity(body));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Can not request body entity", e);
            }
        } else {
            throw new IllegalStateException("Can not set body for request of type "+method);
        }
        return this;
    }

    public ResponseValidator expectResponse() {
        return new HttpComponentsResponseValidator(httpClient, method);
    }
}
