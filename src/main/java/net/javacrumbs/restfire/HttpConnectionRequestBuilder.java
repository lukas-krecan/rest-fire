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
package net.javacrumbs.restfire;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;

public class HttpConnectionRequestBuilder implements PostRequestBuilder {
    private final HttpURLConnection httpURLConnection;
    private Reader body;

    public HttpConnectionRequestBuilder(HttpURLConnection httpURLConnection1) {
        this.httpURLConnection =  httpURLConnection1;
    }

    public RequestBuilder withBody(String body) {
        this.body = new StringReader(body);
        return this;
    }

    public ResponseValidator expectResponse() {
        try {
            if (body!=null) {
                httpURLConnection.setDoOutput(true);
            }
            httpURLConnection.connect();
            if (body!=null) {
                httpURLConnection.getOutputStream();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Can not fire request", e);
        }
        return new HttpConnectionResponseValidator(httpURLConnection);
    }
}
