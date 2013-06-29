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

import net.javacrumbs.restfire.httpcomponents.HttpComponentsRequestFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * This is the entry point to rest-fire library. You can use it this way:
 * <pre>
 *     import static net.javacrumbs.restfire.*;
 *
 *     ...
 *
 *      fire().get().
 *                withUri("https://www.google.com/search").
 *                withQueryParameter("q", "rest-fire").
 *                withHeader("Accept", "text/html").
 *        expectResponse().
 *                havingStatusEqualTo(200).
 *                havingHeader("Content-Type", hasItem(startsWith("text/html"))).
 *                havingBody(containsString("rest-fire"));
 *
 *
 * </pre>
 *
 * For advanced usage use {@link net.javacrumbs.restfire.httpcomponents.HttpComponentsRequestFactory} directly, like this
 *
 * <pre>
 *    private final HttpClient httpClient = new DefaultHttpClient();
 *    private HttpComponentsRequestFactory fire;
 *
 *   {@literal @}Before
 *    public void setUp() {
 *        fire = new HttpComponentsRequestFactory(httpClient, new RequestProcessor() {
 *            public void processRequest(RequestBuilder requestBuilder) {
 *                requestBuilder.withUri("http://localhost:"+port());
 *            }
 *        });
 *    }
 *
 *   {@literal @}Test
 *    public void testPost() {
 *         fire.post()
 *                .withPath("/test")
 *                .withHeader("Accept", "text/plain")
 *                .withQueryParameter("param1", "paramValue")
 *                .withBody("Request body")
 *            .expectResponse()
 *                .havingStatusEqualTo(200)
 *                .havingHeaderEqualTo("Content-type", "text/plain")
 *                .havingBodyEqualTo("Response");
 *    }
 * </pre>
 *
 */
public class RestFire {

    private static final HttpClient httpClient = new DefaultHttpClient();
    private static RequestProcessor requestProcessor;

    /**
     * Use this method to fire HTTP requests.
     * @return
     */
    public static RequestFactory fire() {
        return new HttpComponentsRequestFactory(httpClient, requestProcessor);
    }

    /**
     * Set-up a {@link RequestProcessor} to preprocess all requests. For example
     *
     * <pre>
     *     RestFire.preprocessAllRequests(new RequestProcessor() {
     *            public void processRequest(RequestBuilder requestBuilder) {
     *                requestBuilder.withUri("http://localhost:" + port());
     *            }
     *        });
     *
     * </pre>
     * @param requestProcessor
     */
    public static void preprocessAllRequests(RequestProcessor requestProcessor) {
        RestFire.requestProcessor = requestProcessor;
    }

    /**
     * Resets the library to the original settings.
     */
    public static void resetToDefaultSetting() {
        requestProcessor = null;
    }
}
