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
import net.javacrumbs.restfire.RequestBuilder;
import net.javacrumbs.restfire.RequestFactory;
import net.javacrumbs.restfire.RequestProcessor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Apache HTTP client 4 based request factory. Use this directly class for advanced usage and special HttpClient
 * configuration.
 */
public class HttpComponentsRequestFactory implements RequestFactory {
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
        HttpComponentsRequestBuilder requestBuilder = doCreateRequestBuilder(request);
        preprocessRequest(requestBuilder);
        return requestBuilder;
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
     * @return
     */
    protected HttpComponentsRequestBuilder doCreateRequestBuilder(HttpRequestBase request) {
        return new HttpComponentsRequestBuilder(httpClient, request);
    }
}
