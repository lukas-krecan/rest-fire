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

import net.javacrumbs.restfire.httpcomponents.HttpComponentsMethodBuilder;
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
 */
public class RestFire {

    private static HttpClient httpClient = new DefaultHttpClient();
    private static RequestProcessor requestProcessor;


    public static MethodBuilder fire() {
        return new HttpComponentsMethodBuilder(httpClient, requestProcessor);
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static void setHttpClient(HttpClient httpClient) {
        RestFire.httpClient = httpClient;
    }

    public static void preprocessAllRequests(RequestProcessor requestProcessor) {
        RestFire.requestProcessor = requestProcessor;
    }

    public static void resetToDefaultSetting() {
        httpClient = new DefaultHttpClient();
        requestProcessor = null;
    }
}
