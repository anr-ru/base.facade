/**
 * 
 */
package ru.anr.base.facade.ejb.mdb;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;

import ru.anr.base.tests.facade.BaseWebTestCase;

/**
 * Verifying how the unique instance ID works
 *
 *
 * @author Alexey Romanchuk
 * @created Sep 28, 2015
 *
 */
@ActiveProfiles("production")
public class LoopBaseEventKeyStrategyTest extends BaseWebTestCase {

    /**
     * The value to check
     */
    private static String value;

    /**
     * Setting the value for a first time
     */
    @BeforeClass
    public static void before() {

        value = LoopBaseEventKeyStrategy.getInstanceID();
    }

    /**
     * Use case : we should make sure the instance ID is always the same
     */
    @Test
    public void testGetInstanceID() {

        Assert.assertNotNull(value);
        Assert.assertEquals(value, LoopBaseEventKeyStrategy.getInstanceID());
    }

    /**
     * A sample message
     * 
     * @param id
     *            The ID value (can be a null if no one required)
     * @return The message
     */
    private Message<String> msg(String id) {

        Map<String, Object> hh = (id == null) ? null : toMap(LoopBaseEventKeyStrategy.INSTANCE_HEADER, id);
        return new GenericMessage<String>("", hh);
    }

    /**
     * Use case : a message does not contain the header at all
     */
    @Test
    public void testCheckInstanceCycleNoHeader() {

        jms.clean(queue);

        LoopBaseEventKeyStrategy s = bean(LoopBaseEventKeyStrategy.class);
        s.loop(msg(null), queue, 0L);
        Assert.assertNull(jms.receiveAndConvert(queue));
    }

    /**
     * Use case : a message contains the valid header with a valid value
     */
    @Test
    public void testCheckInstanceCycleGoodHeader() {

        jms.clean(queue);
        LoopBaseEventKeyStrategy s = bean(LoopBaseEventKeyStrategy.class);

        Message<String> m = msg(LoopBaseEventKeyStrategy.getInstanceID());
        s.loop(m, queue, 0L);
        Assert.assertSame(m, jms.receiveAndConvert(queue));
    }

    /**
     * Use case : a message contains the valid header with a wrong value
     */
    @Test
    public void testCheckInstanceCycleWrongHeader() {

        jms.clean(queue);
        LoopBaseEventKeyStrategy s = bean(LoopBaseEventKeyStrategy.class);

        Message<String> m = msg("xxx");

        s.loop(m, queue, 0L);
        Assert.assertNull(jms.receiveAndConvert(queue));
    }
}
