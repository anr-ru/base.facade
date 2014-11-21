/**
 * 
 */
package ru.anr.base.facade.tests;

import java.util.Queue;

import javax.jms.Destination;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description ...
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 22, 2014
 *
 */

public class TestJmsOperationsTest {

    /**
     * Sending and receiving messages
     */
    @Test
    public void test() {

        Destination x = new TestDestination("x");
        Destination y = new TestDestination("y");

        TestJmsOperations jms = new TestJmsOperations();
        jms.setDefaultDestination(y);

        jms.convertAndSend(x, "1");
        Assert.assertEquals("1", jms.receiveAndConvert(x));
        Assert.assertNull(jms.receiveAndConvert(x)); // once more is null
        Assert.assertNull(jms.receiveAndConvert()); // default is null

        jms.convertAndSend("2");
        Assert.assertEquals("2", jms.receiveAndConvert());

        jms.convertAndSend("3");
        jms.convertAndSend("4");

        Queue<Object> q1 = jms.browse("x", null);
        Assert.assertNotNull(q1);
        Assert.assertTrue(q1.isEmpty());

        Queue<Object> q2 = jms.browse(null); // default queue
        Assert.assertNotNull(q2);
        Assert.assertFalse(q2.isEmpty());

        Assert.assertEquals("3", q2.poll());
        Assert.assertEquals("4", q2.poll());

    }

}
