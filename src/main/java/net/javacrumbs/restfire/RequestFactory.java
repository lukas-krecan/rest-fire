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

/**
 * Creates the HTTP requests.
 */
public interface RequestFactory<B extends RequestBuilder<B>> {
    /**
     * Creates POST request.
     *
     * @return
     */
    B post();

    /**
     * Creates PUT request.
     *
     * @return
     */
    B put();

    /**
     * Creates PATCH request.
     *
     * @return
     */
    B patch();

    /**
     * Creates GET request.
     *
     * @return
     */
    B get();

    /**
     * Creates DELETE request.
     *
     * @return
     */
    B delete();

    /**
     * Creates HEAD request.
     *
     * @return
     */
    B head();

    /**
     * Creates OPTIONS request.
     *
     * @return
     */
    B options();

    /**
     * Creates TRACE request.
     *
     * @return
     */
    B trace();

}
