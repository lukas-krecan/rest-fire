package net.javacrumbs.restfire;

public interface BodyContainingRequestBuilder extends RequestBuilder {
    RequestBuilder withBody(String body);

    BodyContainingRequestBuilder withHeader(String name, String value);

    BodyContainingRequestBuilder withQueryParameter(String name, String value);

    BodyContainingRequestBuilder withPath(String uri);
}
