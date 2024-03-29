/*
 * Copyright 2014-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.facade.ejb.mdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import ru.anr.base.services.pattern.StrategyFactory;
import ru.anr.base.services.pattern.StrategyStatistic;

/**
 * A specialized message handler which uses the {@link StrategyFactory} pattern
 * and expects a number of {@link ru.anr.base.services.pattern.Strategy} in a
 * spring context. Each strategy is responsible for processing a message
 * according to its own algorithm.
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 */

public class AbstractStrategyBasedMessageHandler extends AbstractMessageDrivenHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractStrategyBasedMessageHandler.class);

    @Override
    protected void onMessage(Message<String> msg) {

        StrategyFactory factory = bean(StrategyFactory.class);
        StrategyStatistic st = factory.process(msg);

        logger.trace("Strategies applied: {}", st.getAppliedStrategies());
    }
}
