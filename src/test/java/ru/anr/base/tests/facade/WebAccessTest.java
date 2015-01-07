/**
 * 
 */
package ru.anr.base.tests.facade;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;

/**
 * General EJB/Web integration tests.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */

public class WebAccessTest extends BaseWebTestCase {

    /**
     * Client under test
     */
    private final RestClient client = new RestClient();

    /**
     * Test local dao
     */
    @Autowired
    @Qualifier("mydao")
    private MyDao dao;

    /**
     * Integration test to chech EJB/JTA/JMS/JPA integration to work with
     * Glassfish Embedded server.
     * 
     * Transaction annotations (which gives a local transaction) is required for
     * lazy-loaded hibernate objects.
     */
    @Test
    @Transactional
    public void checks() {

        RestClient rest = new RestClient();
        rest.setAccept(MediaType.TEXT_PLAIN);

        ResponseEntity<String> r = rest.get("api/v1/query/2");

        logger.info("Result: {}", r.getBody());
        Assert.assertNotNull(r.getBody());

        sleep(2000); // Waiting JMS Handler to complete

        Long id = Long.valueOf(r.getBody());

        Samples s = dao.findOne(id);

        Assert.assertNotNull(s);
        Assert.assertEquals("2,xxx", s.getName());
    }

    /**
     * GET method
     */
    @Test
    public void testGet() {

        ResponseEntity<String> r = client.get("/api/v1/get?q={query}&variable={x}", "secret", "yyy");

        Assert.assertEquals(HttpStatus.OK, r.getStatusCode());
        logger.info("Response: {}", r.getBody());

        Assert.assertEquals("{\"value\": \"secret\"}", r.getBody());
    }

    /**
     * POST method
     */
    @Test
    public void testPost() {

        ResponseEntity<String> r = client.post("/api/v1/post", "{\"value\": \"secret\"}");

        Assert.assertEquals(HttpStatus.OK, r.getStatusCode());
        logger.info("Response: {}", r.getBody());

        Assert.assertEquals("{\"value\": \"secret\"}", r.getBody());
    }

    /**
     * POST method
     */
    @Test
    public void testPut() {

        ResponseEntity<String> r = client.put("/api/v1/put", "{\"value\": \"secret\"}");

        Assert.assertEquals(HttpStatus.CREATED, r.getStatusCode());
        logger.info("Response: {}", r.getBody());

        Assert.assertNull(r.getBody());
    }

    /**
     * DELETE method
     */
    @Test
    public void testDelete() {

        ResponseEntity<String> r = client.delete("/api/v1/delete/64");

        Assert.assertEquals(HttpStatus.ALREADY_REPORTED, r.getStatusCode());
        logger.info("Response: {}", r.getBody());

        Assert.assertNull(r.getBody());
    }

}
