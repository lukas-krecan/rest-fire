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

import net.javacrumbs.restfire.BodyContainingRequestBuilder;
import net.javacrumbs.restfire.Configuration;
import net.javacrumbs.restfire.MethodBuilder;
import net.javacrumbs.restfire.RequestBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;

public class HttpComponentsMethodBuilder implements MethodBuilder {
    private final HttpClient httpClient;
    private final Configuration configuration;

    public HttpComponentsMethodBuilder(HttpClient httpClient, Configuration configuration) {
        this.httpClient = httpClient;
        this.configuration = configuration;
    }

    public BodyContainingRequestBuilder post() {
        return createRequestBuilder(new HttpPost());
    }

    public BodyContainingRequestBuilder put() {
        return createRequestBuilder(new HttpPut());
    }

    public BodyContainingRequestBuilder patch() {
        return createRequestBuilder(new HttpPatch());
    }

    public RequestBuilder get() {
        return createRequestBuilder(new HttpGet());
    }

    public RequestBuilder delete() {
        return createRequestBuilder(new HttpDelete());
    }

    public RequestBuilder head() {
        return createRequestBuilder(new HttpHead());
    }

    public RequestBuilder options() {
        return createRequestBuilder(new HttpOptions());
    }

    private HttpComponentsRequestBuilder createRequestBuilder(HttpRequestBase request) {
        return new HttpComponentsRequestBuilder(httpClient, request, createUriBuilder());
    }

    private URIBuilder createUriBuilder() {
        try {
            return new URIBuilder(configuration.getDefaultUriPrefix());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can not create URI", e);
        }
    }
}
