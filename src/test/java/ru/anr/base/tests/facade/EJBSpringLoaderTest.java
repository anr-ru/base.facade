/**
 * 
 */
package ru.anr.base.tests.facade;

import java.util.Queue;

import javax.jms.Destination;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;

import ru.anr.base.facade.ejb.EJBSpringLoader;
import ru.anr.base.tests.JmsTests;

/**
 * Description ...
 *
 *
 * @author Alexey Romanchuk
 * @created Jun 11, 2015
 *
 */
@ActiveProfiles("production")
public class EJBSpringLoaderTest extends BaseWebTestCase {

    /**
     * Reference to {@link EJBSpringLoader}
     */
    @Autowired
    private EJBSpringLoader loader;

    /**
     * Reference to {@link JmsTests}
     */
    @Autowired
    private JmsTests jms;

    /**
     * Test method for {@link ru.anr.base.facade.ejb.EJBSpringLoader#init()}.
     */
    @Test
    public void testInit() {

        Assert.assertNotNull(loader);
        Queue<Message<String>> q = jms.queue(bean("queue", Destination.class));
        Assert.assertEquals(2, q.size());
        Message<String> m1 = q.poll();
        Message<String> m2 = q.poll();

        Assert.assertEquals("KEY", m1.getHeaders().get("TYPE_KEY"));
        Assert.assertEquals("KEY", m2.getHeaders().get("TYPE_KEY"));

        Assert.assertEquals("BODY1", m1.getPayload());
        Assert.assertEquals("BODY2", m2.getPayload());

        Assert.assertEquals("VALUE", m1.getHeaders().get("HH"));
        Assert.assertNull(m2.getHeaders().get("HH"));
    }
}
