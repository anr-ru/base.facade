package ru.anr.base.tests.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import ru.anr.base.facade.ejb.EJBSpringLoader;
import ru.anr.base.facade.samples.ejb.StartUpEJB;

import java.util.Queue;

/**
 * Description ...
 *
 * @author Alexey Romanchuk
 * @created Jun 11, 2015
 */
@ActiveProfiles("production")
public class EJBSpringLoaderTest extends BaseWebTestCase {

    /**
     * Test method for {@link ru.anr.base.facade.ejb.EJBSpringLoader#init()}.
     */
    @Test
    public void testInit() {

        jms.clean(queue);
        EJBSpringLoader loader = bean(StartUpEJB.class);

        Assertions.assertNotNull(loader);
        Queue<Message<String>> q = jms.queue(queue);
        Assertions.assertEquals(2, q.size());
        Message<String> m1 = q.poll();
        Message<String> m2 = q.poll();

        Assertions.assertEquals("KEY", m1.getHeaders().get("TYPE_KEY"));
        Assertions.assertEquals("KEY", m2.getHeaders().get("TYPE_KEY"));

        Assertions.assertEquals("BODY1", m1.getPayload());
        Assertions.assertEquals("BODY2", m2.getPayload());

        Assertions.assertEquals("VALUE", m1.getHeaders().get("HH"));
        Assertions.assertNull(m2.getHeaders().get("HH"));
    }
}
