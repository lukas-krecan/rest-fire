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

import net.javacrumbs.restfire.MethodBuilder;
import net.javacrumbs.restfire.PostRequestBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

public class HttpComponentsMethodBuilder implements MethodBuilder {
    private final HttpClient httpClient = new DefaultHttpClient();
    private final String defaultUriPrefix;

    public HttpComponentsMethodBuilder(String defaultUrlPrefix) {
        this.defaultUriPrefix = defaultUrlPrefix;
    }

    public PostRequestBuilder postTo(String uri) {
        return new HttpComponentsRequestBuilder(httpClient, new HttpPost(constructUri(uri)));
    }

    private URI constructUri(String uri) {
        if (defaultUriPrefix !=null) {
            return URI.create(defaultUriPrefix + uri);
        } else {
            return URI.create(uri);
        }
    }
}
