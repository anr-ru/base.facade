/*
 * Copyright 2014-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.tests.multithreading.ThreadJob;

import java.util.function.Consumer;

/**
 * A special type of {@link ThreadJob} which handles http 5xx errors (to sees
 * the error body).
 *
 * @author Alexey Romanchuk
 * @created Jul 3, 2015
 */

public class HttpJob extends ThreadJob {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(HttpJob.class);

    /**
     * The constructor takes the {@link RestClient} as the first argument
     *
     * @param rest The {@link RestClient}
     * @param job  The {@link ThreadJob} interface to run
     */
    public HttpJob(RestClient rest, Consumer<Object[]> job) {

        super(job);
        add(rest); // will be as the first argument
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String processException(Throwable ex) {

        String rs;
        if (ex instanceof HttpStatusCodeException) {

            HttpStatusCodeException http = (HttpStatusCodeException) ex;

            logger.error("http error: {}/{}", http.getStatusCode(), http.getResponseBodyAsString());
            rs = http.getResponseBodyAsString();

        } else {
            rs = super.processException(ex);
        }
        return rs;
    }
}
