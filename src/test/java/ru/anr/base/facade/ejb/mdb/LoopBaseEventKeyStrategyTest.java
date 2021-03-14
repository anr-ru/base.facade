package ru.anr.base.facade.ejb.mdb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import ru.anr.base.tests.facade.BaseWebTestCase;

import java.util.Map;

/**
 * Verifying how the unique instance ID works
 *
 * @author Alexey Romanchuk
 * @created Sep 28, 2015
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
    @BeforeEach
    public void before() {

        value = LoopBaseEventKeyStrategy.getInstanceID();
    }

    /**
     * Use case : we should make sure the instance ID is always the same
     */
    @Test
    public void testGetInstanceID() {

        Assertions.assertNotNull(value);
        Assertions.assertEquals(value, LoopBaseEventKeyStrategy.getInstanceID());
    }

    /**
     * A sample message
     *
     * @param id The ID value (can be a null if no one required)
     * @return The message
     */
    private Message<String> msg(String id) {

        Map<String, Object> hh = (id == null) ? null : toMap(LoopBaseEventKeyStrategy.INSTANCE_HEADER, id);
        return new GenericMessage<>("", hh);
    }

    /**
     * Use case : a message does not contain the header at all
     */
    @Test
    public void testCheckInstanceCycleNoHeader() {

        jms.clean(queue);

        LoopBaseEventKeyStrategy s = bean(LoopBaseEventKeyStrategy.class);
        s.loop(msg(null), queue, 0L);
        Assertions.assertNull(jms.receiveAndConvert(queue));
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
        Assertions.assertSame(m, jms.receiveAndConvert(queue));
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
        Assertions.assertNull(jms.receiveAndConvert(queue));
    }
}
