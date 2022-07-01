package ru.anr.base.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpServerErrorException;
import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.tests.multithreading.ThreadJob;

import java.util.function.Consumer;

/**
 * A special class of {@link ThreadJob} which handles http 5xx errors (to sees
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
        if (ex instanceof HttpServerErrorException) {

            HttpServerErrorException http = (HttpServerErrorException) ex;

            logger.error("http server error: {}", http.getResponseBodyAsString());
            rs = http.getResponseBodyAsString();

        } else {
            rs = super.processException(ex);
        }
        return rs;
    }
}
