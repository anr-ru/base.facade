/**
 * 
 */
package ru.anr.base.facade.ejb.mdb;

import org.springframework.messaging.Message;

import ru.anr.base.services.BaseServiceImpl;
import ru.anr.base.services.pattern.StrategyConfig;
import ru.anr.base.services.pattern.StrategyConfig.StrategyModes;

/**
 * The base strategy for the case when difference between strategies is defined
 * by a value of some header.
 *
 *
 * @author Alexey Romanchuk
 * @created May 2, 2015
 *
 */

public class BaseEventKeyStrategy extends BaseServiceImpl implements MessageStrategy {

    /**
     * Type key
     */
    public static final String TYPE_KEY = "TYPE_KEY";

    /**
     * A value for key used here
     */
    private String keyValue;

    /**
     * {@inheritDoc}
     */
    @Override
    public StrategyConfig check(Message<String> o, Object... params) {

        String value = (String) o.getHeaders().get(TYPE_KEY);
        return new StrategyConfig(safeEquals(value, keyValue), o, StrategyModes.Normal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message<String> process(Message<String> o, StrategyConfig cfg) {

        return o; // a loop
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param keyValue
     *            the keyValue to set
     */
    public void setKeyValue(String keyValue) {

        this.keyValue = keyValue;
    }
}
