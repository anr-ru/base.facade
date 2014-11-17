/**
 * 
 */
package ru.anr.base.facade.tests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

/**
 * Description ...
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

        logger.info("Result: {}", r.getBody());
        Assert.assertEquals("{\"code\":0,\"message\":\"response,2\"}", r.getBody());
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

}
