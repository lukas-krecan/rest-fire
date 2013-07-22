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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static net.javacrumbs.restfire.RestFire.fire;

/**
 * JSON assert.
 */
public class JsonTest {

    @Before
    public void setUp() {
        initJadler();
        RestFire.preprocessAllRequests(new RequestProcessor() {
            @Override
            public void processRequest(RequestBuilder requestBuilder) {
                requestBuilder.withPort(port());
            }
        });
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    @Test
    public void testJsonEquals() {
        onRequest().respond().withBody("{\n" +
                "   \"employees\":[\n" +
                "      {\n" +
                "         \"firstName\":\"John\",\n" +
                "         \"lastName\":\"Doe\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"firstName\":\"Anna\",\n" +
                "         \"lastName\":\"Smith\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"firstName\":\"Peter\",\n" +
                "         \"lastName\":\"Jones\"\n" +
                "      }\n" +
                "   ]\n" +
                "}");

        //compare the whole document, ignore part of it
        fire().get().to("/path").expectResponse().havingBody(jsonEquals("{\n" +
                "   \"employees\":[\n" +
                "      {\n" +
                "         \"firstName\":\"John\",\n" +
                "         \"lastName\":\"Doe\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"firstName\":\"Anna\",\n" +
                "         \"lastName\":\"Smith\"\n" +
                "      },\n" +
                "      \"${json-unit.ignore}\"\n" +
                "   ]\n" +
                "}"));


        ///Compare just one element
        fire().get().to("/path").expectResponse().havingBody(jsonPartEquals("employees[1].firstName", "\"Anna\""));

        ///Compare part of the document
        fire().get().to("/path").expectResponse().havingBody(jsonPartEquals("employees[1]",
                        "      {" +
                        "         \"firstName\":\"Anna\"," +
                        "         \"lastName\":\"Smith\"" +
                        "      }"));


    }
}
