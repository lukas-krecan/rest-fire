package net.javacrumbs.restfire;

import java.util.Collection;
import java.util.List;

/**
 * Response wrapper
 */
public interface Response {
    /**
     * HTTP response status.
     *
     * @return
     */
    public int getStatus();

    /**
     * HTTP response headers
     *
     * @return
     */
    public Headers getHeaders();


    /**
     * Body as String decoded using response encoding
     *
     * @return
     */
    public String getBody();

    /**
     * Row bytes from body.
     *
     * @return
     */
    public byte[] getRawBody();

    /**
     * Duration in millis.
     */
    public long getDuration();

    /**
     * Returns response validator.
     *
     * @return
     */
    public ResponseValidator getValidator();

    /**
     * Fluent synonym thet returns validator.
     */
    public ResponseValidator is();

    /**
     * Response headers wrapper.
     */
    public static interface Headers {
        /**
         * Returns header names or an empty collection.
         *
         * @return
         */
        public Collection<String> getHeaderNames();

        /**
         * Returns header names or an empty collection.
         * The names case is not changed by the library.
         *
         * @return
         */
        public Collection<String> getHeaderNamesCaseSensitive();

        /**
         * Returns header values for given header or an empty collection.
         * Case-insensitive regarding the header name.
         *
         * @param name
         * @return
         */
        public List<String> getHeaders(String name);

        /**
         * Returns header values for given header or an empty collection.
         * Case-sensitive regarding the header name.
         *
         * @param name
         * @return
         */
        public List<String> getHeadersCaseSensitive(String name);
    }
}
