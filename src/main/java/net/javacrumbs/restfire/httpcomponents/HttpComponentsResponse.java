/**
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.restfire.httpcomponents;

import net.javacrumbs.restfire.Response;
import net.javacrumbs.restfire.ResponseValidator;
import net.javacrumbs.restfire.impl.DefaultHeaders;
import net.javacrumbs.restfire.impl.DefaultResponseValidator;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Wraps HttpComponents response.
 */
public class HttpComponentsResponse implements Response {
    private final byte[] responseBody;
    private final Charset charset;
    private final long duration;
    private final Headers headers;
    private final int statusCode;

    public HttpComponentsResponse(HttpClient httpClient, HttpRequestBase method) {
        try {
            long executionStart = System.currentTimeMillis();
            HttpResponse response = httpClient.execute(method);
            duration = System.currentTimeMillis() - executionStart;

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                responseBody = EntityUtils.toByteArray(responseEntity);
                charset = getCharset(responseEntity);
            } else {
                responseBody = null;
                charset = null;
            }
            headers = buildHeaderMap(response);
            statusCode = response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            throw new IllegalArgumentException("Can not execute method", e);
        } finally {
            method.releaseConnection();
        }
    }

    private static Charset getCharset(HttpEntity entity) {
        Charset charset = null;
        ContentType contentType = ContentType.get(entity);
        if (contentType != null) {
            charset = contentType.getCharset();
        }
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET;
        }
        return charset;
    }


    public int getStatus() {
        return statusCode;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getBody() {
        return responseBody != null ? new String(responseBody, charset) : "";
    }

    public byte[] getRawBody() {
        return responseBody;
    }

    public long getDuration() {
        return duration;
    }

    public ResponseValidator getValidator() {
        return new DefaultResponseValidator(this);
    }

    public ResponseValidator is() {
        return getValidator();
    }

    private static Headers buildHeaderMap(HttpResponse response) {
        Header[] allHeaders = response.getAllHeaders();
        DefaultHeaders result = new DefaultHeaders();
        for (Header header : allHeaders) {
            result.addHeader(header.getName(), header.getValue());
        }
        return result;
    }
}
