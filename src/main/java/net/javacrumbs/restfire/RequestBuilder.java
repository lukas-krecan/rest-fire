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
public interface RequestBuilder{
    /**
     * Adds request header.
     * @param name
     * @param value
     * @return
     */
    RequestBuilder withHeader(String name, String value);

    /**
     * Adds query parameter/
     * @param name
     * @param value
     * @return
     */
    RequestBuilder withQueryParameter(String name, String value);

    /**
     * Sets URI path for this request/
     * @param uri
     * @return
     */
    RequestBuilder withPath(String uri);

    /**
     * Sets the whole URI for the request/
     * @param uri
     * @return
     */
    RequestBuilder withUri(URI uri);

    /**
     * Sets the whole URI for the request/
     * @param uri
     * @return
     */
    RequestBuilder withUri(String uri);

    /**
     * Executes request and switches to response validation mode.
     * @return
     */
    ResponseValidator expectResponse();


}
