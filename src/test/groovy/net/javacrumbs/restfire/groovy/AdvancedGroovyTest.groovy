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
package net.javacrumbs.restfire.groovy

import net.javacrumbs.restfire.RequestProcessor
import net.javacrumbs.restfire.httpcomponents.HttpComponentsRequestFactory
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test

import static net.jadler.Jadler.*

public class AdvancedGroovyTest {
    private final HttpClient httpClient = new DefaultHttpClient(/** HTTP client config**/);

    @Before
    void setUp() {
        initJadler()
    }

    def fire() {
        new HttpComponentsRequestFactory(httpClient, {it.withPort(port())} as RequestProcessor)
    }

    @After
    void tearDown() {
        closeJadler()
    }


    @Test
    void testPost() {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo("/test")
                .havingHeaderEqualTo("Accept", "text/plain")
                .havingParameterEqualTo("param1", "paramValue")
                .havingBodyEqualTo("Request body")
                .respond().withStatus(200).withHeader("Content-Type", "text/plain").withBody("Response")

        fire().post().to("/test")
                .withHeader("Accept", "text/plain")
                .withQueryParameter("param1", "paramValue")
                .withBody("Request body")
                .expectResponse()
                .havingStatusEqualTo(200)
                .havingHeaderEqualTo("Content-type", "text/plain")
                .havingBodyEqualTo "Response"
    }
}
