package ru.anr.base.tests.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.transaction.annotation.Transactional;
import ru.anr.base.facade.ejb.mdb.MessageStrategy;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;
import ru.anr.base.services.pattern.StrategyConfig;
import ru.anr.base.services.pattern.StrategyConfig.StrategyModes;

/**
 * General EJB/Web integration tests.
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 */

public class MessageStrategyTest extends BaseWebTestCase {

    /**
     * Test local dao
     */
    @Autowired
    private MyDao dao;

    private static final class TestStrategy implements MessageStrategy {

        /**
         * {@inheritDoc}
         */
        @Override
        public StrategyConfig check(Message<String> o, Object... params) {

            return new StrategyConfig(true, o, StrategyModes.Normal);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Message<String> process(Message<String> o, StrategyConfig cfg) {

            return o;
        }

    }

    /**
     * Tests for
     */
    @Test
    @Transactional
    public void checks() {

        TestStrategy s = new TestStrategy();

        Samples o = dao.save(new Samples());

        Message<String> msg = new GenericMessage<>("", s.toHeaders(o));

        Samples ox = s.extractObject(msg, dao);
        Assertions.assertEquals(o, ox);
    }
}
