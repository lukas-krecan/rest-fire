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
package net.javacrumbs.restfire.extension;

import net.javacrumbs.restfire.RequestProcessor;
import net.javacrumbs.restfire.httpcomponents.HttpComponentsRequestBuilder;
import net.javacrumbs.restfire.httpcomponents.HttpComponentsRequestFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public class ExtendedHttpComponentsRequestFactory extends HttpComponentsRequestFactory<ExtendedHttpComponentsRequestBuilder> {
    /**
     * Creates HttpComponentsRequestFactory.
     *
     * @param httpClient
     * @param requestProcessor is called just after each {@link net.javacrumbs.restfire.RequestBuilder} creation. Can be used for default configuration setting.
     */
    public ExtendedHttpComponentsRequestFactory(HttpClient httpClient, RequestProcessor requestProcessor) {
        super(httpClient, requestProcessor);
    }

    @Override
    protected HttpComponentsRequestBuilder doCreateRequestBuilder(HttpClient httpClient, HttpRequestBase request) {
        return new ExtendedHttpComponentsRequestBuilder(httpClient, request);
    }
}
