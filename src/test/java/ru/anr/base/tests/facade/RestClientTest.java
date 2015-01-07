/**
 * 
 */
package ru.anr.base.tests.facade;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.tests.BaseTestCase;

/**
 * {@link RestClient} tests.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 *
 */
@ContextConfiguration(classes = RestClientTest.class)
public class RestClientTest extends BaseTestCase {

    /**
     * Client under test
     */
    private RestClient client;

    /**
     * Initialization
     */
    @Override
    @Before
    public void setUp() {

        client = new RestClient();
    }

    /**
     * Tests for {@link RestClient#getBaseUrl()}
     */
    @Test
    public void testGetBaseUrl() {

        // Default
        Assert.assertEquals("http://localhost:8080", client.getBaseUrl());

        // Default ports
        client.setPort(80);
        Assert.assertEquals("http://localhost", client.getBaseUrl());

        client.setPort(443);
        Assert.assertEquals("http://localhost", client.getBaseUrl());

        // Schema and host
        client.setPort(22);
        client.setSchema("ssh");
        client.setHost("google.com");

        Assert.assertEquals("ssh://google.com:22", client.getBaseUrl());
    }

    /**
     * Tests for {@link RestClient#getUri(String)}
     */
    @Test
    public void testGetUri() {

        Assert.assertEquals("http://localhost:8080/ping", client.getUri("/ping"));
        Assert.assertEquals("http://localhost:8080/ping", client.getUri("ping"));

        Assert.assertEquals("https://google.com/xxx", client.getUri("https://google.com/xxx"));
    }

    /**
     * Tests for connections
     */
    @Test
    public void testConnect() {

        ResponseEntity<String> r1 = client.get("https://google.com");
        Assert.assertEquals(HttpStatus.OK, r1.getStatusCode());

        ResponseEntity<String> r2 = client.get("http://google.com");
        Assert.assertEquals(HttpStatus.OK, r2.getStatusCode());
    }
}
