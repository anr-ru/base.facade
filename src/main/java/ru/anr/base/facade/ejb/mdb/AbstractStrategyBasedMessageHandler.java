/**
 * 
 */
package ru.anr.base.facade.ejb.mdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import ru.anr.base.services.pattern.StrategyFactory;
import ru.anr.base.services.pattern.StrategyStatistic;

/**
 * A specialized message handler, which uses {@link StrategyFactory} pattern and
 * expects a number of {@link ru.anr.base.services.pattern.Strategy} in a spring
 * context. Each strategy is responsible for processing a message according to
 * its own algorithm.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */

public class AbstractStrategyBasedMessageHandler extends AbstractMessageDrivenHandler {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractStrategyBasedMessageHandler.class);

    /**
     * Factory for strategies
     */
    @Autowired
    private StrategyFactory factory;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMessage(Message<String> msg) {

        StrategyStatistic st = factory.process(msg);

        logger.debug("Strategies processed: {}", st.getAppliedStrategies());
    }

    // /////////////////////////////////////////////////////////////////////////
    // ///getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * To override injection
     * 
     * @param factory
     *            the factory to set
     */
    public void setFactory(StrategyFactory factory) {

        this.factory = factory;
    }

}
