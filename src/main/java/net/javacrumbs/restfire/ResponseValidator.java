package net.javacrumbs.restfire;

public interface ResponseValidator {
    ResponseValidator havingStatus(int status);

    ResponseValidator havingBodyEqualTo(String body);
}
