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

    @Test
    public void testToAllInclusive() throws URISyntaxException {
        requestBuilder.to("https://bob:bobby@www.lunatech.com:8080/file;p=1?q=2#third");

        assertEquals(URI.create("https://bob:bobby@www.lunatech.com:8080/file;p=1?q=2#third"), requestBuilder.getUriBuilder().build());
    }

    @Test
    public void testDoubleTo() throws URISyntaxException {
        requestBuilder.to("http://test/path1").to("/path2");

        assertEquals(URI.create("http://test/path2"), requestBuilder.getUriBuilder().build());
    }

    @Test
    public void testDoubleTo2() throws URISyntaxException {
        requestBuilder.to("http://test/path1/").to("path2");

        assertEquals(URI.create("http://test/path1/path2"), requestBuilder.getUriBuilder().build());
    }
    /**
     * Inspired by http://blog.lunatech.com/2009/02/03/what-every-web-developer-must-know-about-url-encoding
     * @throws URISyntaxException
     */
    @Test
    public void testToWeirdEncoding() throws URISyntaxException {
        requestBuilder.to("http://example.com/a%2Fb%3Fc");

        assertEquals(URI.create("http://example.com/a%2Fb%3Fc"), requestBuilder.getUriBuilder().build());
    }

    /**
     * Inspired by http://blog.lunatech.com/2009/02/03/what-every-web-developer-must-know-about-url-encoding
     * @throws URISyntaxException
     */
    @Test
    public void testToWeirdEncoding2() throws URISyntaxException {
        requestBuilder.to("http://example.com/blue%2Fred%3Fand+green");

        assertEquals(URI.create("http://example.com/blue%2Fred%3Fand+green"), requestBuilder.getUriBuilder().build());
    }
}
