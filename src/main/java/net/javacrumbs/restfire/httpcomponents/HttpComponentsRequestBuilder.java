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
import net.javacrumbs.restfire.RequestProcessor;
import net.javacrumbs.restfire.ResponseValidator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configures {@link HttpClient}.
 */
public class HttpComponentsRequestBuilder implements BodyContainingRequestBuilder {
    private final HttpClient httpClient;
    private final HttpRequestBase request;
    private URIBuilder uriBuilder;

    public HttpComponentsRequestBuilder(HttpClient httpClient, HttpRequestBase request) {
        this.httpClient = httpClient;
        this.request = request;
        uriBuilder = new URIBuilder(URI.create("http://localhost:8080"));
    }

    public RequestBuilder withBody(String body) {
        if (request instanceof HttpEntityEnclosingRequestBase) {
            try {
                ((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(body));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Can not request body entity", e);
            }
        } else {
            throw new IllegalStateException("Can not set body for request of type "+ request);
        }
        return this;
    }

    public BodyContainingRequestBuilder to(String address) {
        return to(URI.create(address));
    }

    public BodyContainingRequestBuilder to(URI address) {
        if (address.getScheme()!=null && address.getScheme().length()>0) {
            uriBuilder.setScheme(address.getScheme());
        }
        //if host is defined, we have to rewrite port
        if (address.getHost()!=null){
            uriBuilder.setPort(address.getPort());
        }
        if (address.getHost()!=null) {
            uriBuilder.setHost(address.getHost());
        }
        if (address.getPath()!=null) {
            uriBuilder.setPath(address.getPath());
        }
        if (address.getQuery()!=null) {
            uriBuilder.setQuery(address.getQuery());
        }
        if (address.getFragment()!=null) {
            uriBuilder.setFragment(address.getFragment());
        }
        return this;
    }

    public BodyContainingRequestBuilder withHeader(String name, String value) {
        request.addHeader(name, value);
        return this;
    }

    public BodyContainingRequestBuilder withQueryParameter(String name, String value) {
        uriBuilder.addParameter(name, value);
        return this;
    }

    public BodyContainingRequestBuilder withPath(String path) {
        uriBuilder.setPath(path);
        return this;
    }

    public BodyContainingRequestBuilder withPort(int port) {
        uriBuilder.setPort(port);
        return this;
    }

    public BodyContainingRequestBuilder withHost(String host) {
        uriBuilder.setHost(host);
        return this;
    }

    public BodyContainingRequestBuilder withScheme(String scheme) {
        uriBuilder.setScheme(scheme);
        return this;
    }

    public BodyContainingRequestBuilder withUri(URI uri) {
        uriBuilder = new URIBuilder(uri);
        return this;
    }

    public BodyContainingRequestBuilder withUri(String uri) {
        return withUri(URI.create(uri));
    }

    public BodyContainingRequestBuilder with(RequestProcessor requestProcessor) {
        requestProcessor.processRequest(this);
        return this;
    }

    public ResponseValidator expectResponse() {
        try {
            request.setURI(uriBuilder.build());
            return doCreateResponseValidator();
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
     * @return
     */
    protected HttpComponentsResponseValidator doCreateResponseValidator() {
        return new HttpComponentsResponseValidator(httpClient, request);
    }
}
