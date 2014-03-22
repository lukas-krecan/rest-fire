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
package net.javacrumbs.restfire.impl;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class DefaultHeadersTest {
    private final DefaultHeaders headers = new DefaultHeaders();

    @Test
    public void addHeadersIsCaseInsensitive() {
        headers.addHeader("rAnDom", "value1");
        headers.addHeader("RaNdOm", "value2");

        assertThat(headers.getHeaders("random"), equalTo(asList("value1", "value2")));
        assertThat(headers.getHeadersCaseSensitive("RaNdOm"), equalTo(asList("value2")));
    }

    @Test
    public void maintainsCase() {
        headers.addHeader("lowerCamelCase", "value");
        headers.addHeader("CamelCase", "value");
        headers.addHeader("rAnDom", "value");
        headers.addHeader("Content-Type", "value");

        assertThat(headers.getHeaderNames(), hasItems("Lowercamelcase", "Camelcase", "Random", "Content-type"));
        assertThat(headers.getHeaderNamesCaseSensitive(), hasItems("lowerCamelCase", "CamelCase", "rAnDom", "Content-Type"));
    }

    @Test
    public void emptyReturnsEmptyCllections() {
        assertThat(headers.getHeaderNames(), empty());
        assertThat(headers.getHeaderNamesCaseSensitive(), empty());
        assertThat(headers.getHeaders("test"), empty());
        assertThat(headers.getHeadersCaseSensitive("test"), empty());
    }

}
