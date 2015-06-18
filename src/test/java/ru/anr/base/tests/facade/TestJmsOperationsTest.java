/**
 * 
 */
package ru.anr.base.tests.facade;

import java.util.Enumeration;
import java.util.Queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import ru.anr.base.tests.TestDestination;
import ru.anr.base.tests.TestJmsOperations;

/**
 * Tests for {@link TestJmsOperations}.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 22, 2014
 *
 */

public class TestJmsOperationsTest {

    /**
     * Creates a new text message
     * 
     * @param text
     *            The text of a message
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
        Assert.assertEquals(m, jms.receiveAndConvert(x));
        Assert.assertNull(jms.receiveAndConvert(x)); // once more is null
        Assert.assertNull(jms.receiveAndConvert()); // default is null

        m = msg("2");
        jms.convertAndSend(m);
        Assert.assertEquals(m, jms.receiveAndConvert());

        Message<String> m3 = msg("3");
        Message<String> m4 = msg("4");

        jms.convertAndSend(m3);
        jms.convertAndSend(m4);

        Queue<Object> q1 = jms.browse("x", null);
        Assert.assertNotNull(q1);
        Assert.assertTrue(q1.isEmpty());

        Queue<Object> q2 = jms.browse(null); // default queue
        Assert.assertNotNull(q2);
        Assert.assertFalse(q2.isEmpty());

        Assert.assertEquals(m3, q2.poll());
        Assert.assertEquals(m4, q2.poll());

    }

    /**
     * Tests for Queue 'Browsing'
     */
    @Test
    public void testBrowsing() {

        TestDestination d = new TestDestination("QUEUE");

        TestJmsOperations jms = new TestJmsOperations();
        jms.convertAndSend(d, new GenericMessage<String>("XXX"));

        String rs = jms.browseSelected(d, null, new BrowserCallback<String>() {

            @SuppressWarnings("unchecked")
            @Override
            public String doInJms(Session session, QueueBrowser browser) throws JMSException {

                Enumeration<TextMessage> e = browser.getEnumeration();
                return e.hasMoreElements() ? e.nextElement().getText() : null;
            }
        });
        Assert.assertEquals("XXX", rs);
    }
}
