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
    
Advanced configuration
----------------------
If you need advanced configuration, use HttpComponentsRequestFactory directly
    
    private final HttpClient httpClient = new DefaultHttpClient();
    private HttpComponentsRequestFactory fire;

    @Before
    public void setUp() {
        fire = new HttpComponentsRequestFactory(httpClient, new RequestProcessor() {
            @Override
            public void processRequest(RequestBuilder requestBuilder) {
                requestBuilder.withUri("http://localhost:"+port());
            }
        });
    }

    @Test
    public void testPost() {
        fire.post()
                .withPath("/test")
                .withHeader("Accept", "text/plain")
                .withQueryParameter("param1", "paramValue")
                .withBody("Request body")
            .expectResponse()
                .havingStatusEqualTo(200)
                .havingHeaderEqualTo("Content-type", "text/plain")
                .havingBodyEqualTo("Response");
    }

