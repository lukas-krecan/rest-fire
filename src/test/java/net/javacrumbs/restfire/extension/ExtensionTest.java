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
package net.javacrumbs.restfire.extension;


import net.javacrumbs.restfire.RequestBuilder;
import net.javacrumbs.restfire.RequestProcessor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;

public class ExtensionTest {

    @Before
    public void setUp() {
        initJadler();
    }

    @After
    public void tearDown(){
        closeJadler();
    }

    @Test
    public void testExtension() {
        onRequest().respond().withStatus(200).withHeader("Test", "Bam");
        fire().post().expectResponse().havingStatusEqualTo(200).havingSuperSpecialResponse();
    }

    protected ExtendedHttpComponentsRequestFactory fire() {
        return new ExtendedHttpComponentsRequestFactory(new DefaultHttpClient(), new RequestProcessor() {
            @Override
            public void processRequest(RequestBuilder requestBuilder) {
                requestBuilder.withPort(port());
            }
        });
    }
}