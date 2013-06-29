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

public class RestFire {

    private static Configuration configuration = new Configuration();

    private static HttpClient httpClient = new DefaultHttpClient();

    public static MethodBuilder fire() {
        return new HttpComponentsMethodBuilder(httpClient, configuration);
    }

    public static Configuration configure() {
        return configuration;
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static void setHttpClient(HttpClient httpClient) {
        RestFire.httpClient = httpClient;
    }
}
