package net.javacrumbs.restfire;

public interface ResponseValidator {
    ResponseValidator havingStatusEqualTo(int status);

    ResponseValidator havingBodyEqualTo(CharSequence body);
}
