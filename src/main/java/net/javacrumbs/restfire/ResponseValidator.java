package net.javacrumbs.restfire;

import org.hamcrest.Matcher;

import java.util.List;

public interface ResponseValidator {
    ResponseValidator havingStatusEqualTo(int status);

    ResponseValidator havingStatus(Matcher<Integer> statusMatcher);

    ResponseValidator havingBodyEqualTo(String body);

    ResponseValidator havingBody(Matcher<String> bodyMatcher);

    ResponseValidator havingHeaderEqualTo(String name, String value);

    ResponseValidator havingHeader(final String name, final Matcher<? super List<String>> matcher);
}
