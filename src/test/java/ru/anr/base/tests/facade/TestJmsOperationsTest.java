package ru.anr.base.tests.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import ru.anr.base.tests.MockTextMessageImpl;
import ru.anr.base.tests.TestDestination;
import ru.anr.base.tests.TestJmsOperations;

import javax.jms.Destination;
import java.util.Enumeration;
import java.util.Queue;

/**
 * Tests for {@link TestJmsOperations}.
 *
 * @author Alexey Romanchuk
 * @created Nov 22, 2014
 */

public class TestJmsOperationsTest {

    /**
     * Creates a new text message
     *
     * @param text The text of a message
     * @return The text message
     */
    private Message<String> msg(String text) {

        return new GenericMessage<>(text);
    }

    /**
     * Sending and receiving messages
     */
    @Test
    public void test() {

        Destination x = new TestDestination("x");
        Destination y = new TestDestination("y");

        TestJmsOperations jms = new TestJmsOperations();
        jms.setDefaultDestination(y);

        Message<String> m = msg("1");
        jms.convertAndSend(x, m);
        Assertions.assertEquals(m, jms.receiveAndConvert(x));
        Assertions.assertNull(jms.receiveAndConvert(x)); // once more is null
        Assertions.assertNull(jms.receiveAndConvert()); // default is null

        m = msg("2");
        jms.convertAndSend(m);
        Assertions.assertEquals(m, jms.receiveAndConvert());

        Message<String> m3 = msg("3");
        Message<String> m4 = msg("4");

        jms.convertAndSend(m3);
        jms.convertAndSend(m4);

        Queue<Object> q1 = jms.browse("x", null);
        Assertions.assertNotNull(q1);
        Assertions.assertTrue(q1.isEmpty());

        Queue<Object> q2 = jms.browse(null); // default queue
        Assertions.assertNotNull(q2);
        Assertions.assertFalse(q2.isEmpty());

        Assertions.assertEquals(m3, q2.poll());
        Assertions.assertEquals(m4, q2.poll());

    }

    /**
     * Tests for Queue 'Browsing'
     */
    @Test
    public void testBrowsing() {

        TestDestination d = new TestDestination("QUEUE");

        TestJmsOperations jms = new TestJmsOperations();
        jms.convertAndSend(d, new GenericMessage<>("XXX"));

        String rs = jms.browseSelected(d, null, (session, browser) -> {
            @SuppressWarnings("unchecked")
            Enumeration<MockTextMessageImpl> e = browser.getEnumeration();
            return e.hasMoreElements() ? e.nextElement().getMessage().getPayload() : null;
        });
        Assertions.assertEquals("XXX", rs);
    }
}
