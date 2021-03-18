package ru.anr.base.tests.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.tests.HttpJob;
import ru.anr.base.tests.multithreading.ThreadExecutor;

/**
 * Testing API via RESTClient
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 */

public class ApiTest extends BaseWebTestCase {

    /**
     * API Command 'ping' testing : GET
     */
    @Test
    public void pingGet() {

        RestClient client = new RestClient();
        ResponseEntity<String> r = client.get("/api/v1/ping/2");

        logger.debug("Result: {}", r.getBody());
        Assertions.assertEquals("{\"code\":0,\"message\":\"response,2\"}", r.getBody());

        // Changin to xml as expected type
        client.setAccept(MediaType.APPLICATION_XML);
        r = client.get("/api/v1/ping/2");

        logger.debug("Result: {}", r.getBody());
        Assertions.assertEquals("<?xml version='1.0' encoding='UTF-8'?><pong code=\"0\" message=\"response,2\"/>",
                r.getBody());
    }

    /**
     * API Command 'ping' testing : POST
     */
    @Test
    public void pingPost() {

        RestClient client = new RestClient();
        ResponseEntity<String> r = client.post("/api/v1/ping/2", "{\"message\":\"hello\"}");

        logger.info("Result: {}", r.getBody());
        Assertions.assertEquals("{\"code\":0,\"message\":\"hello,2\"}", r.getBody());
    }

    /**
     * Api Command throws exception
     */
    @Test
    public void exception() {

        RestClient client = new RestClient();
        try {
            client.delete("/api/v1/delete");
            Assertions.fail();
        } catch (HttpClientErrorException ex) {

            logger.info("Result: {}", ex.getResponseBodyAsString());

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            Assertions.assertEquals("{\"code\":100,\"message\":\"Shit happend\"}", ex.getResponseBodyAsString());
        }
    }

    /**
     * 404: Not found exception
     */
    @Test
    public void notFoundException() {

        RestClient client = new RestClient();
        try {
            client.put("/api/v1/notfound/reason", "");
            Assertions.fail();
        } catch (HttpClientErrorException ex) {
            logger.info("Result: {}", ex.getResponseBodyAsString());

            Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            Assertions.assertEquals("{\"code\":1,\"error_id\":\"what\",\"message\":\"reason\"}",
                    ex.getResponseBodyAsString());
        }
    }

    /**
     * 403 Forbidden
     */
    @Test
    public void forbidden403() {

        RestClient client = new RestClient();
        try {
            client.post("/api/v1/denied", "");
            Assertions.fail();
        } catch (HttpClientErrorException ex) {
            logger.info("Result: {}", ex.getResponseBodyAsString());

            Assertions.assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
            Assertions.assertEquals("{\"code\":1,\"message\":\"Unable to access this ping\"}",
                    ex.getResponseBodyAsString());
        }
    }

    /**
     * 401 Unauthorized
     */
    @Test
    public void unauthorized401() {

        RestClient client = new RestClient();
        try {
            client.get("/api/v1/unauth");
            Assertions.fail();
        } catch (HttpClientErrorException ex) {
            logger.info("Result: {}", ex.getResponseBodyAsString());

            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
            Assertions.assertEquals("{\"code\":1,\"message\":\"Wrong password or alike\"}", ex.getResponseBodyAsString());
        }
    }

    /**
     * 400 Bad Request if API
     */
    @Test
    public void badRequest400() {

        RestClient client = new RestClient();
        try {
            client.delete("/api/v1/api");
            Assertions.fail();
        } catch (HttpClientErrorException ex) {
            logger.info("Result: {}", ex.getResponseBodyAsString());

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            Assertions.assertEquals("{\"code\":-1,\"error_id\":\"shit.happened\",\"message\":\"Shit happened\"}",
                    ex.getResponseBodyAsString());
        }
    }

    /**
     * 400 Bad Request if Validation
     */
    @Test
    public void methodNotAllowed405() {

        RestClient client = new RestClient();
        try {
            client.delete("/api/v1/validate");
            Assertions.fail();
        } catch (HttpClientErrorException ex) {
            logger.info("Result: {}", ex.getResponseBodyAsString());

            Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, ex.getStatusCode());
            Assertions.assertEquals("{\"code\":1,\"message\":\"Request method 'DELETE' not supported\"}",
                    ex.getResponseBodyAsString());
        }
    }

    /**
     * 400 Bad Request if Validation
     */
    @Test
    public void badRequest400Validation() {

        RestClient client = new RestClient();
        try {
            client.put("/api/v1/validate", "");
            Assertions.fail();
        } catch (HttpClientErrorException ex) {
            logger.info("Result: {}", ex.getResponseBodyAsString());

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            Assertions.assertEquals("{\"code\":-1,\"error_id\":\"validation\",\"message\":\"[must not be null]\"}",
                    ex.getResponseBodyAsString());
        }
    }

    /**
     * 500 Internal server error
     */
    @Test
    public void internalError500() {

        RestClient client = new RestClient();
        try {
            client.put("/api/v1/system", "");
            Assertions.fail();
        } catch (HttpServerErrorException ex) {
            logger.info("Result: {}", ex.getResponseBodyAsString());

            Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
            Assertions.assertEquals("{\"code\":1,\"message\":\"Just runtime exception\"}", ex.getResponseBodyAsString());
        }
    }

    /**
     * API Command 'ping' testing : GET
     */
    @Test
    public void multithreaded() {

        ThreadExecutor exec = new ThreadExecutor();

        exec.add(new HttpJob(new RestClient(), x -> {

            ThreadExecutor.sleep(100, 100);

            for (int i = 0; i < 100; i++) {
                RestClient c = (RestClient) x[0];

                ResponseEntity<String> r = c.get("/api/v1/ping/2");
                Assertions.assertEquals("{\"code\":0,\"message\":\"response,2\"}", r.getBody());
                ThreadExecutor.sleep(10, 50);
            }
        }));

        exec.add(new HttpJob(new RestClient(), x -> {

            ThreadExecutor.sleep(100, 100);

            for (int i = 0; i < 100; i++) {
                RestClient c = (RestClient) x[0];

                ResponseEntity<String> r = c.get("/api/v1/ping/5");
                Assertions.assertEquals("{\"code\":0,\"message\":\"response,5\"}", r.getBody());
                ThreadExecutor.sleep(10, 50);
            }
        }));

        exec.start();
        Assertions.assertTrue(exec.waitNotError());
    }
}
