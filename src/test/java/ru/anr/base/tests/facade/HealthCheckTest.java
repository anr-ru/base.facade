package ru.anr.base.tests.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import ru.anr.base.facade.web.api.RestClient;

/**
 * Tests for HealthCheck.
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 */

public class HealthCheckTest extends BaseWebTestCase {

    /**
     * Client under test
     */
    private final RestClient client = new RestClient();

    /**
     * '/healthcheck'
     */
    @Test
    public void testGet() {

        ResponseEntity<String> r = client.get("/healthcheck");

        Assertions.assertEquals(HttpStatus.OK, r.getStatusCode());
        logger.info("Response: {}", r.getBody());

        assertContains(r.getBody(), "{\"status\" : \"UP\", \"message\":\"");

        assertContains(client.get("/healthcheck/false").getBody(), "{\"status\" : \"UP\", \"message\":\"");
    }

    /**
     * '/healthz'
     */
    @Test
    public void testGetHealthz() {

        ResponseEntity<String> r = client.get("/healthz");

        Assertions.assertEquals(HttpStatus.OK, r.getStatusCode());
        logger.info("Response: {}", r.getBody());

        assertContains(r.getBody(), "{\"status\" : \"UP\", \"message\":\"");
    }


    /**
     * '/health' if failure
     */
    @Test
    public void testGetIfFailure() {

        try {
            client.get("/healthcheck/true");
            Assertions.fail();
        } catch (HttpServerErrorException ex) {

            Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getStatusCode());
            logger.info("Response: {}", ex.getResponseBodyAsString());

            assertContains(ex.getResponseBodyAsString(), "{\"status\" : \"FAILURE\"");
        }
    }

}
