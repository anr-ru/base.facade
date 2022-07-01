package ru.anr.base.facade.web.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import ru.anr.base.tests.BaseTestCase;

/**
 * {@link RestClient} tests.
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
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
    @BeforeEach
    public void setUp() {

        client = new RestClient();
    }

    /**
     * Tests for {@link RestClient#getBaseUrl()}
     */
    @Test
    public void testGetBaseUrl() {

        // Default
        Assertions.assertEquals("http://localhost:8080", client.getBaseUrl());

        // Default ports
        client.setPort(80);
        Assertions.assertEquals("http://localhost", client.getBaseUrl());

        client.setPort(443);
        Assertions.assertEquals("http://localhost", client.getBaseUrl());

        // Schema and host
        client.setPort(22);
        client.setSchema("ssh");
        client.setHost("google.com");

        Assertions.assertEquals("ssh://google.com:22", client.getBaseUrl());
    }

    /**
     * Tests for {@link RestClient#getUri(String)}
     */
    @Test
    public void testGetUri() {

        Assertions.assertEquals("http://localhost:8080/ping", client.getUri("/ping"));
        Assertions.assertEquals("http://localhost:8080/ping", client.getUri("ping"));

        Assertions.assertEquals("https://google.com/xxx", client.getUri("https://google.com/xxx"));
    }

    /**
     * Tests for connections
     */
    @Test
    public void testConnect() {

        ResponseEntity<String> r1 = client.get("https://google.com");
        Assertions.assertEquals(HttpStatus.OK, r1.getStatusCode());

        ResponseEntity<String> r2 = client.get("http://google.com");
        Assertions.assertEquals(HttpStatus.OK, r2.getStatusCode());
    }
}
