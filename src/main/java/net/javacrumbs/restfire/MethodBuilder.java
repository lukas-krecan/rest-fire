package net.javacrumbs.restfire;

public interface MethodBuilder {
    BodyContainingRequestBuilder post();

    BodyContainingRequestBuilder put();

    BodyContainingRequestBuilder patch();

    RequestBuilder get();

    RequestBuilder delete();

    RequestBuilder head();

    RequestBuilder options();

}
