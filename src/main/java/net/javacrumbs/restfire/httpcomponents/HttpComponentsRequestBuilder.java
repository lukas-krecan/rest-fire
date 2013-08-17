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
import net.javacrumbs.restfire.RequestProcessor;
import net.javacrumbs.restfire.ResponseValidator;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configures {@link HttpClient}.
 */
public class HttpComponentsRequestBuilder implements RequestBuilder {
    private final HttpClient httpClient;
    private final HttpRequestBase request;
    private URIBuilder uriBuilder;

    public HttpComponentsRequestBuilder(HttpClient httpClient, HttpRequestBase request) {
        this.httpClient = httpClient;
        this.request = request;
        uriBuilder = new URIBuilder(URI.create("http://localhost:8080"));
    }

    public RequestBuilder withBody(String body) {
        setBody(new StringEntity(body, getContentType()));
        return this;
    }

    private ContentType getContentType() {
        Header contentTypeHeader = request.getFirstHeader("Content-Type");
        if (contentTypeHeader!=null) {
            return ContentType.parse(contentTypeHeader.getValue());
        } else {
            return null;
        }
    }

    public RequestBuilder withBody(byte[] body) {
        setBody(new ByteArrayEntity(body));
        return this;
    }

    private void setBody(HttpEntity entity) {
        if (request instanceof HttpEntityEnclosingRequestBase) {
           ((HttpEntityEnclosingRequestBase) request).setEntity(entity);
        } else {
            throw new IllegalStateException("Can not set body for request of type " + request);
        }
    }

    public RequestBuilder to(String address) {
        return to(URI.create(address));
    }

    public RequestBuilder to(URI address) {
        uriBuilder = new URIBuilder(buildUri().resolve(address));
        return this;
    }

    public RequestBuilder withHeader(String name, String value) {
        request.addHeader(name, value);
        return this;
    }

    public RequestBuilder withQueryParameter(String name, String value) {
        uriBuilder.addParameter(name, value);
        return this;
    }

    public RequestBuilder withPath(String path) {
        uriBuilder.setPath(path);
        return this;
    }

    public RequestBuilder withPort(int port) {
        uriBuilder.setPort(port);
        return this;
    }

    public RequestBuilder withHost(String host) {
        uriBuilder.setHost(host);
        return this;
    }

    public RequestBuilder withScheme(String scheme) {
        uriBuilder.setScheme(scheme);
        return this;
    }

    public RequestBuilder withFragment(String fragment) {
        uriBuilder.setFragment(fragment);
        return this;
    }

    public RequestBuilder withUri(URI uri) {
        uriBuilder = new URIBuilder(uri);
        return this;
    }

    public RequestBuilder withUri(String uri) {
        return withUri(URI.create(uri));
    }

    public RequestBuilder with(RequestProcessor requestProcessor) {
        requestProcessor.processRequest(this);
        return this;
    }

    public ResponseValidator expectResponse() {
        request.setURI(buildUri());
        return doCreateResponseValidator();
    }

    private URI buildUri() {
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can not build URI", e);
        }
    }

    //for test
    URIBuilder getUriBuilder() {
        return uriBuilder;
    }

    /**
     * Creates response validator.
     * @return response validator
     */
    protected HttpComponentsResponseValidator doCreateResponseValidator() {
        return new HttpComponentsResponseValidator(httpClient, request);
    }
}
