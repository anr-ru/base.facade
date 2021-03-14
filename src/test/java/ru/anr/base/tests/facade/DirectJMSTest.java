package ru.anr.base.tests.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import ru.anr.base.tests.AbstractGlassfishWebTestCase;
import ru.anr.base.tests.JmsTests;

/**
 * Direct messaging tests.
 *
 * @author Alexey Romanchuk
 * @created Dec 12, 2016
 */
@ContextConfiguration(locations = "classpath:direct-jms-context.xml")
public class DirectJMSTest extends AbstractGlassfishWebTestCase {

    /**
     * {@link JmsTests}
     */
    @Autowired
    protected JmsOperations jms;

    /**
     * Tests for direct sending and receiving
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testJMSSendAndReceive() {

        Message<String> m = new GenericMessage<>("XXX");
        jms.convertAndSend("XxxQueue", m);

        Message<String> mx = (Message<String>) jms.receiveAndConvert("XxxQueue");
        Assertions.assertNotNull(mx);
        Assertions.assertEquals("XXX", mx.getPayload());
    }
}
