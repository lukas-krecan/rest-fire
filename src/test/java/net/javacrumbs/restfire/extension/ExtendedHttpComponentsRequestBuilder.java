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

import net.javacrumbs.restfire.httpcomponents.HttpComponentsRequestBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Example of extending basic capabilities.
 */
public class ExtendedHttpComponentsRequestBuilder extends HttpComponentsRequestBuilder<ExtendedHttpComponentsRequestBuilder> {

    public ExtendedHttpComponentsRequestBuilder(HttpClient httpClient, HttpRequestBase request) {
        super(httpClient, request);
    }

    public ExtendedResponseValidator expectResponse() {
        return new ExtendedResponseValidator(getResponse());
    }
}
