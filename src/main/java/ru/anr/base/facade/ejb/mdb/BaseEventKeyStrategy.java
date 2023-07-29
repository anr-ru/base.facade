/*
 * Copyright 2014-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ru.anr.base.facade.ejb.mdb;

import org.springframework.messaging.Message;
import ru.anr.base.services.BaseServiceImpl;
import ru.anr.base.services.pattern.StrategyConfig;

/**
 * The base strategy for the case when difference between strategies is defined
 * by a value of some header.
 *
 * @author Alexey Romanchuk
 * @created May 2, 2015
 */

public class BaseEventKeyStrategy extends BaseServiceImpl implements MessageStrategy {

    /**
     * Type key
     */
    public static final String TYPE_KEY = "TYPE_KEY";

    /**
     * The key for routing messages
     */
    public static final String ROUTING_KEY = "ROUTING_KEY";

    /**
     * A value for key used here
     */
    private String keyValue;

    /**
     * {@inheritDoc}
     */
    @Override
    public StrategyConfig check(Message<String> o, Object... params) {
        return headerCheck(o, TYPE_KEY, keyValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message<String> process(Message<String> o, StrategyConfig cfg) {
        return o; // a loop
    }

    ///////////////////////////////////////////////////////////////////////////
    ///// getters/setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param keyValue the keyValue to set
     */
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
