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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class HttpComponentsRequestBuilderTest {

    private final HttpClient httpClient = mock(HttpClient.class);

    private HttpRequestBase request = new HttpGet();

    private HttpComponentsRequestBuilder requestBuilder = new HttpComponentsRequestBuilder(httpClient, request);

    @Test
    public void testToOverride() throws URISyntaxException {
        requestBuilder.withUri("http://localhost:8080");
        requestBuilder.to("https://www.google.com/test?q=1");

        assertEquals(URI.create("https://www.google.com/test?q=1"), requestBuilder.getUriBuilder().build());
    }

    @Test
    public void testToPathWithParams() throws URISyntaxException {
        requestBuilder.withUri("http://localhost:8080");
        requestBuilder.to("/test?q=1");

        assertEquals(URI.create("http://localhost:8080/test?q=1"), requestBuilder.getUriBuilder().build());
    }
}
