/**
 * 
 */
package ru.anr.base.tests.facade;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import ru.anr.base.tests.RestClient;

/**
 * Testing API via RESTClient
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
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
        Assert.assertEquals("{\"code\":0,\"message\":\"response,2\"}", r.getBody());

        // Changin to xml as expected type
        client.setAccept(MediaType.APPLICATION_XML);
        r = client.get("/api/v1/ping/2");

        logger.debug("Result: {}", r.getBody());
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?><pong code=\"0\" message=\"response,2\"/>",
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
        Assert.assertEquals("{\"code\":0,\"message\":\"hello,2\"}", r.getBody());
    }

    /**
     * Api Command throws exception
     */
    @Test
    public void exception() {

        RestClient client = new RestClient();
        ResponseEntity<String> r = client.delete("/api/v1/delete");

        logger.info("Result: {}", r.getBody());
        Assert.assertEquals("{\"code\":100}", r.getBody());
    }
}
