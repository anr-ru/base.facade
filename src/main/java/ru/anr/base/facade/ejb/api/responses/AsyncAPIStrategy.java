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

package ru.anr.base.facade.ejb.api.responses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.facade.ejb.api.AsyncAPIHeaders;
import ru.anr.base.facade.ejb.mdb.BaseEventKeyStrategy;
import ru.anr.base.services.api.APICommandFactory;
import ru.anr.base.services.pattern.StrategyConfig;

import java.util.HashMap;
import java.util.Locale;

/**
 * The base asynchronous API handler
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 */
public class AsyncAPIStrategy extends BaseEventKeyStrategy {

    private static final Logger logger = LoggerFactory.getLogger(AsyncAPIStrategy.class);

    /**
     * The default constructor
     *
     * @param keyName The name of the key
     */
    public AsyncAPIStrategy(String keyName) {
        setKeyValue(keyName);
    }

    /**
     * {@link APICommandFactory}
     */
    @Autowired
    private APICommandFactory apis;

    /**
     * The name of the strategy ID header
     */
    private static final String STRATEGY = AsyncAPIHeaders.API_STRATEGY_ID.name();

    /**
     * The name of the version header
     */
    private static final String VERSION = AsyncAPIHeaders.API_VERSION.name();

    /**
     * The name of the method header
     */
    private static final String METHOD = AsyncAPIHeaders.API_METHOD.name();

    /**
     * {@link APIResponseManager}
     */
    @Autowired
    private APIResponseManager responses;

    /**
     * {@inheritDoc}
     */
    @Override
    public Message<String> process(Message<String> m, StrategyConfig cfg) {

        logger.debug("Processing API command: {}", m);
        APICommand cmd = buildCommand(m);

        try {
            APICommand cmdx = apis.process(cmd);
            responses.respond(cmd, cmdx);

        } catch (Exception ex) {
            responses.error(cmd, ex);
            throw ex; // re-throw to complete the transaction roll-back
        }
        return null;
    }

    public static APICommand buildCommand(Message<String> m) {

        APICommand cmd = new APICommand(
                m.getHeaders().get(STRATEGY, String.class),
                m.getHeaders().get(VERSION, String.class));

        cmd.setContexts(new HashMap<>(m.getHeaders())); // Immutable map !
        if (!isEmpty(m.getPayload())) {
            cmd.setRawModel(m.getPayload());
        }
        cmd.method(
                nullSafe(m.getHeaders().get(METHOD, String.class),
                        n -> n.toUpperCase(Locale.getDefault())).orElse(null)
        );
        return cmd;
    }
}
