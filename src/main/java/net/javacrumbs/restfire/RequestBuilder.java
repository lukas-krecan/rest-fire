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
package net.javacrumbs.restfire;

import java.net.URI;

/**
 * Interface for configuring the request.
 */
public interface RequestBuilder  {

    /**
     * Sends request to address. Works in a clever way as to not override
     * values set by {@link #withHost(String)}, {@link #withPort(int)}  etc.
     * See {@link URI#resolve(URI)} for more details.
     * @param address
     * @return
     */
    RequestBuilder to(String address);

    /**
     * Sends request to address. Works in a clever way as to not override
     * values set by {@link #withHost(String)}, {@link #withPort(int)}  etc.
     * See {@link URI#resolve(URI)} for more details.
     * @param address
     * @return
     */
    RequestBuilder to(URI address);

    /**
     * Sets request header.
     * @param name
     * @param value
     * @return
     */
    RequestBuilder withHeader(String name, String value);

    /**
    * Sets request header with multiple values
    * @param name
    * @param values
    * @return
    */
    RequestBuilder withHeaders(String name, String... values);

    /**
     * Adds query parameter.
     * @param name
     * @param value
     * @return
     */
    RequestBuilder withQueryParameter(String name, String value);

    /**
     * Sets URI path for the request.
     * @param uri
     * @return
     */
    RequestBuilder withPath(String uri);

    /**
     * Sets port for the request.
     * @param port
     * @return
     */
    RequestBuilder withPort(int port);

    /**
     * Sets port for the request.
     * @param host
     * @return
     */
    RequestBuilder withHost(String host);

    /**
     * Sets scheme for the request.
     * @param scheme
     * @return
     */
    RequestBuilder withScheme(String scheme);

    /**
     * Sets URI fragment.
     * @param fragment
     * @return
     */
    RequestBuilder withFragment(String fragment);


    /**
     * Sets the whole URI for the request. Default value is http://locahost:8080.
     * @param uri
     * @return
     */
    RequestBuilder withUri(URI uri);

    /**
     * Sets the whole URI for the request. Default value is http://locahost:8080.
     * @param uri
     * @return
     */
    RequestBuilder withUri(String uri);

    /**
     * Sets request body.
     *
     * @param body
     * @return
     */
    RequestBuilder withBody(String body);

    /**
     * Sets request body.
     *
     * @param body
     * @return
     */
    RequestBuilder withBody(byte[] body);

    /**
     * Advanced configuration. RequestProcess can set multiple parameters at once.
     * @param requestProcessor
     * @return
     */
    RequestBuilder with(RequestProcessor requestProcessor);

    /**
     * Executes request and switches to response validation mode.
     * @return
     */
    ResponseValidator expectResponse();


}
