package net.javacrumbs.restfire;

import org.hamcrest.Matcher;

public interface ResponseValidator {
    ResponseValidator havingStatusEqualTo(int status);

    ResponseValidator havingStatus(Matcher<Integer> statusMatcher);

    ResponseValidator havingBodyEqualTo(String body);

    ResponseValidator havingBody(Matcher<String> bodyMatcher);

}
