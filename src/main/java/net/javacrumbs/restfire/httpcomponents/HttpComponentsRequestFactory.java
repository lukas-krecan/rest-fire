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

import net.javacrumbs.restfire.RequestBuilder;
import net.javacrumbs.restfire.RequestFactory;
import net.javacrumbs.restfire.RequestProcessor;
import net.javacrumbs.restfire.ResponseValidator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;

/**
 * Apache HTTP client 4 based request factory. Use this directly class for advanced usage and special HttpClient
 * configuration.
 */
public class HttpComponentsRequestFactory<B extends RequestBuilder<B>> implements RequestFactory<B> {
    private final HttpClient httpClient;
    private final RequestProcessor requestProcessor;

    /**
     * Creates HttpComponentsRequestFactory.
     * @param httpClient
     * @param requestProcessor is called just after each {@link RequestBuilder} creation. Can be used for default configuration setting.
     */
    public HttpComponentsRequestFactory(HttpClient httpClient, RequestProcessor requestProcessor) {
        this.httpClient = httpClient;
        this.requestProcessor = requestProcessor;
    }

    public B post() {
        return createRequestBuilder(new HttpPost());
    }

    public B put() {
        return createRequestBuilder(new HttpPut());
    }

    public B patch() {
        return createRequestBuilder(new HttpPatch());
    }

    public B get() {
        return createRequestBuilder(new HttpGet());
    }

    public B delete() {
        return createRequestBuilder(new HttpDelete());
    }

    public B head() {
        return createRequestBuilder(new HttpHead());
    }

    public B options() {
        return createRequestBuilder(new HttpOptions());
    }

    public B trace() {
        return createRequestBuilder(new HttpTrace());
    }

    private B createRequestBuilder(HttpRequestBase request) {
        HttpComponentsRequestBuilder requestBuilder = doCreateRequestBuilder(httpClient, request);
        preprocessRequest(requestBuilder);
        return (B)requestBuilder;
    }

    /**
     * Called after {@link RequestBuilder} creation.
     * @param requestBuilder
     */
    protected void preprocessRequest(HttpComponentsRequestBuilder requestBuilder) {
        if (requestProcessor!=null) {
            requestProcessor.processRequest(requestBuilder);
        }
    }

    /**
     * Creates {@link RequestBuilder}.
     * @param request
     * @param httpClient
     * @return
     */
    protected HttpComponentsRequestBuilder doCreateRequestBuilder(HttpClient httpClient, HttpRequestBase request) {
        return new HttpComponentsRequestBuilder(httpClient, request);
    }
}
