package net.javacrumbs.restfire;

import java.util.List;
import java.util.Map;

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
    public Map<String, List<String>> getHeaders();


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
     * @return
     */
    public ResponseValidator getValidator();
}
