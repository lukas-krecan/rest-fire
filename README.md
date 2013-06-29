REST fire
=========

Simple DSL for REST API testing. It's basically a thin wrapper around [Apache HTTP client 4](https://hc.apache.org/httpcomponents-client-ga/)
that makes the API easier to use in integration test. 

It's a simpler alternative to [REST Assured](https://code.google.com/p/rest-assured/) with more straightforward syntax.

Basic usage
-----------
    import static net.javacrumbs.restfire.RestFire.fire;
    
    ...
    
    @Test
    public void testGoogleSearch() {
        fire().get()
                .withUri("https://www.google.com/search")
                .withQueryParameter("q", "rest-fire")
                .withHeader("Accept", "text/html")
        .expectResponse()
                .havingStatusEqualTo(200)
                .havingHeader("Content-Type", hasItem(startsWith("text/html")))
                .havingBody(containsString("rest-fire"));
    }
    

Maven dependency
----------------
    <dependency>
      <groupId>net.javacrumbs.rest-fire</groupId>
      <artifactId>rest-fire</artifactId>
      <version>0.0.1</version>
      <scope>test</scope>
    </dependency>
    
Default setting
---------------
To set default values for requests, use RequestProcessor
    
    //sets default URI for all requests
    RestFire.preprocessAllRequests(new RequestProcessor() {
            public void processRequest(RequestBuilder requestBuilder) {
                requestBuilder.withUri("http://localhost:" + port());
            }
    });
