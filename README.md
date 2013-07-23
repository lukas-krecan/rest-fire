REST fire
=========

Simple DSL for REST API testing. It's basically a thin wrapper around [Apache HTTP client 4](https://hc.apache.org/httpcomponents-client-ga/)
that makes the API easier to use in integration test. 

It's a simpler alternative to [REST Assured](https://code.google.com/p/rest-assured/) with more straightforward syntax
inspired by [Jadler](http://jadler.net).

Basic usage
-----------
    import static net.javacrumbs.restfire.RestFire.fire;
    
    ...
    
    @Test
    public void testGoogleSearch() {
        fire().get().to("https://www.google.com/search?q=rest-fire")
                .withHeader("Accept", "text/html")
        .expectResponse()
                .havingStatusEqualTo(200)
                .havingHeader("Content-Type", hasItem(startsWith("text/html")))
                .havingBody(containsString("rest-fire"));
    }
    

Default setting
---------------
To set default values for requests, use RequestProcessor
    
    //sets default URI for all requests
    RestFire.preprocessAllRequests(new RequestProcessor() {
            public void processRequest(RequestBuilder requestBuilder) {
                requestBuilder.withPort(port());
            }
    });

Combo setting
-------------
Usually you want to set some default setting. It's possible to do it in the following way

        @Test
        public void testCombo() {
            fire().post().with(defaultSettings()).expectResponse().havingStatusEqualTo(200);
        }

        private RequestProcessor defaultSettings() {
            return new RequestProcessor() {
                @Override
                public void processRequest(RequestBuilder requestBuilder) {
                    requestBuilder.withPath("/test").withHeader("Header", "value");
                }
            };
        }

Advanced configuration
----------------------
If you need advanced configuration, use HttpComponentsRequestFactory directly and write your own fire method
    
    //your custom setting of HttpClient
    private final HttpClient httpClient = new DefaultHttpClient(/** HTTP client config**/);


    // your own fire method    
    private RequestFactory fire() {
        return new HttpComponentsRequestFactory(httpClient, null);
    }

    @Test
    public void testPost() {
        fire().post().to("/test")
                .withHeader("Accept", "text/plain")
                .withQueryParameter("param1", "paramValue")
                .withBody("Request body")
            .expectResponse()
                .havingStatusEqualTo(200)
                .havingHeaderEqualTo("Content-type", "text/plain")
                .havingBodyEqualTo("Response");
    }


JSON comparison
---------------
REST fire does not support JSON comparison, but you can use [JsonUnit](https://github.com/lukas-krecan/JsonUnit) in
the following way (you just have to add the dependency)

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


Maven dependency
----------------
    <dependency>
      <groupId>net.javacrumbs.rest-fire</groupId>
      <artifactId>rest-fire</artifactId>
      <version>0.0.4</version>
      <scope>test</scope>
    </dependency>

License
-------
REST fire is licensed under [Apache 2.0 licence](https://www.apache.org/licenses/LICENSE-2.0).
