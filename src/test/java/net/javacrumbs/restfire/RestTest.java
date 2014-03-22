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

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static net.jadler.Jadler.verifyThatRequest;
import static net.jadler.matchers.HeaderRequestMatcher.requestHeader;
import static net.javacrumbs.hamcrest.logger.HamcrestLoggerMatcher.log;
import static net.javacrumbs.restfire.RestFire.fire;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RestTest {

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
        RestFire.resetToDefaultSetting();
    }


    @Test
    public void testPost() {
        doSimpleTestWithRequestBody("POST", fire().post());
    }

    @Test
    public void testPut() {
        doSimpleTestWithRequestBody("PUT", fire().put());
    }

    @Test
    public void testPatch() {
        doSimpleTestWithRequestBody("PATCH", fire().patch());
    }

    @Test
    public void testCombo() {
        onRequest().havingPathEqualTo("/test").havingHeaderEqualTo("Header", "value").respond().withStatus(200);

        fire().post().with(defaultSettings()).expectResponse().havingStatusEqualTo(200);
    }

    @Test
    public void testToParams() {
        onRequest().havingPathEqualTo("/test").havingParameterEqualTo("param1", "value1").respond().withStatus(200);

        fire().post().to("/test?param1=value1").expectResponse().havingStatusEqualTo(200);
    }

    private RequestProcessor defaultSettings() {
        return new RequestProcessor() {
            @Override
            public void processRequest(RequestBuilder requestBuilder) {
                requestBuilder.withPath("/test").withHeader("Header", "value");
            }
        };
    }

    @Test
    public void testPostDifferentCode() {
        onRequest().havingPathEqualTo("/test").havingBodyEqualTo("bla bla").respond().withBody("Ble ble").withStatus(200);
        try {
            fire().post().withPath("/test").withBody("bla bla").expectResponse().havingStatusEqualTo(201);
            fail("Error expected");
        } catch (AssertionError e) {
            assertEquals(
                    "Expected different status code\n" +
                            "Expected: is <201>\n" +
                            "     but: was <200>",
                    e.getMessage()
            );
        }
    }

    @Test
    public void testPostDifferentBody() {
        onRequest().havingPathEqualTo("/test").havingBodyEqualTo("bla bla").respond().withBody("Ble ble").withStatus(200);
        try {
            fire().post().withPath("/test").withBody("bla bla").expectResponse().havingStatusEqualTo(200).havingBodyEqualTo("Ble ble2");
            fail("Error expected");
        } catch (AssertionError e) {
            assertEquals(
                    "Expected different body\n" +
                            "Expected: is \"Ble ble2\"\n" +
                            "     but: was \"Ble ble\"",
                    e.getMessage()
            );
        }
    }

    @Test
    public void testPostDifferentBodyPrefix() {
        onRequest().havingPathEqualTo("/test").havingBodyEqualTo("bla bla").respond().withBody("Ble ble").withStatus(200);
        try {
            fire().post().withPath("/test").withBody("bla bla").expectResponse().havingStatusEqualTo(200).havingBody(startsWith("X"));
            fail("Error expected");
        } catch (AssertionError e) {
            assertEquals(
                    "Expected different body\n" +
                            "Expected: a string starting with \"X\"\n" +
                            "     but: was \"Ble ble\"",
                    e.getMessage()
            );
        }
    }

    @Test
    public void testPostDifferentHeader() {
        onRequest().havingPathEqualTo("/test").havingBodyEqualTo("bla bla")
                .respond().withBody("Ble ble").withStatus(200).withHeader("Content-Type", "text/plain");
        try {
            fire().post().withPath("/test").withBody("bla bla").expectResponse().havingHeaderEqualTo("content-type", "text/html");
            fail("Error expected");
        } catch (AssertionError e) {
            assertEquals(
                    "Expected different header 'content-type'\n" +
                            "Expected: a collection containing \"text/html\"\n" +
                            "     but: was \"text/plain\"",
                    e.getMessage()
            );
        }
    }

    @Test
    public void testMultipleHeaders() {
        onRequest().havingPathEqualTo("/test").havingHeader("header", equalTo(asList("one", "two"))).respond().withHeader("header", "three").withHeader("header", "four").withStatus(200);
        fire().post().withPath("/test").withHeaders("header", "one", "two").expectResponse().havingStatusEqualTo(200).havingHeader("header", equalTo(asList("three", "four")));
    }

    @Test
    public void testGetResponse() {
        onRequest().havingPathEqualTo("/test").havingHeader("header", equalTo(asList("one", "two"))).respond().withHeader("header", "three").withHeader("header", "four").withStatus(200);
        Response response = fire().post().withPath("/test").withHeaders("header", "one", "two").getResponse();
        assertThat(response.getHeaders().getHeaders("header"), equalTo(asList("three", "four")));
        response.is().havingStatusEqualTo(200).havingHeader("header", equalTo(asList("three", "four")));
    }

    @Test
    public void testPostStreamBody() {
        onRequest().havingPathEqualTo("/test").havingRawBodyEqualTo(new byte[]{1, 2, 3}).respond().withBody(new byte[]{4, 5, 6}).withStatus(200);
        fire().post().withPath("/test").withBody(new byte[]{1, 2, 3}).expectResponse().havingStatusEqualTo(200).havingRawBody(equalTo(new byte[]{4, 5, 6}));
    }

    @Test
    public void testPostEncoding() {
        onRequest().havingPathEqualTo("/test").havingBodyEqualTo("Příliš žluťoučký kůň").respond().withContentType("text/plain; charset=CP1250").withEncoding(Charset.forName("CP1250")).withBody("Úpěl ďábelské ódy.").withStatus(200);
        fire().post().withPath("/test").withHeader("Content-Type", "text/plain; charset=ISO-8859-2").withBody("Příliš žluťoučký kůň")
                .expectResponse().havingStatusEqualTo(200).havingBodyEqualTo("Úpěl ďábelské ódy.");
    }

    @Test
    public void testPostNoHeader() {
        onRequest().havingPathEqualTo("/test").havingBodyEqualTo("bla bla")
                .respond().withStatus(200);
        try {
            fire().post().withPath("/test").withBody("bla bla").expectResponse().havingHeaderEqualTo("content-type", "text/html");
            fail("Error expected");
        } catch (AssertionError e) {
            assertEquals(
                    "Expected different header 'content-type'\n" +
                            "Expected: a collection containing \"text/html\"\n" +
                            "     but: ",
                    e.getMessage()
            );
        }
    }

    @Test
    public void testCookie() {
        onRequest().havingPathEqualTo("/root/test").respond().withStatus(200);

        fire().get().withPath("/root/test").withHeader("Cookie", "Name=value").expectResponse();

        verifyThatRequest().havingHeaderEqualTo("Cookie", "Name=value").receivedOnce();
    }

    @Test
    public void testToSlow() {
        onRequest().havingPathEqualTo("/test").respond().withDelay(10, TimeUnit.MILLISECONDS);
        try {
            fire().post().withPath("/test").expectResponse().havingResponseTimeInMillis(lessThan(1L));
            fail("Error expected");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), startsWith("Unexpected response time\nExpected: a value less than <1L>"));
        }
    }

    @Test
    public void testGet() {
        doSimpleTest("GET", fire().get());
    }

    @Test
    public void testDelete() {
        doSimpleTest("DELETE", fire().delete());
    }

    @Test
    public void testOptions() {
        doSimpleTest("OPTIONS", fire().options());
    }

    @Test
    public void testTrace() {
        doSimpleTest("TRACE", fire().trace());
    }

    @Test
    public void testHead() {
        doSimpleTest("HEAD", fire().head());
    }

    public void doSimpleTest(String method, RequestBuilder fireRequest) {
        onRequest()
                .havingMethodEqualTo(method)
                .havingPathEqualTo("/test")
                .havingHeaderEqualTo("Accept", "text/plain")
                .havingParameterEqualTo("param1", "paramValue")
                .respond().withStatus(200).withHeader("Content-Type", "text/plain");

        fireRequest
                .to("/test")
                .withHeader("Accept", "text/plain")
                .withQueryParameter("param1", "paramValue")
                .expectResponse()
                .havingStatusEqualTo(200)
                .havingHeaderEqualTo("Content-type", "text/plain")
                .havingResponseTimeInMillis(lessThan(100L))
                .havingBody(is(""));

        verifyThatRequest().that(log(requestHeader("Accept", hasItem(equalTo("text/plain"))))).receivedOnce();
    }

    public void doSimpleTestWithRequestBody(String method, RequestBuilder fireRequest) {
        onRequest()
                .havingMethodEqualTo(method)
                .havingPathEqualTo("/test")
                .havingHeaderEqualTo("Accept", "text/plain")
                .havingParameterEqualTo("param1", "paramValue")
                .havingBodyEqualTo("Request body")
                .respond().withStatus(200).withHeader("Content-Type", "text/plain").withBody("Response");

        fireRequest
                .to("/test")
                .withHeader("Accept", "text/plain")
                .withQueryParameter("param1", "paramValue")
                .withBody("Request body")
                .expectResponse()
                .havingStatusEqualTo(200)
                .havingHeaderEqualTo("Content-type", "text/plain")
                .havingBodyEqualTo("Response");
    }

}
