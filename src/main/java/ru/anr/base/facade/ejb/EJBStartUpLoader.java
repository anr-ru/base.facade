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
package ru.anr.base.facade.ejb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import ru.anr.base.facade.ejb.mdb.BaseEventKeyStrategy;
import ru.anr.base.facade.ejb.mdb.LoopBaseEventKeyStrategy;

import javax.annotation.PostConstruct;
import javax.jms.Destination;
import java.util.List;
import java.util.Map;

/**
 * A parent for EJB StartUp beans that need to do some work during their start.
 * It provides some tools for initial messages for queues if necessary.
 *
 * <p>
 * Add the @Singleton and @Startup annotations in descendants.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 */
public abstract class EJBStartUpLoader extends EJBContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(EJBStartUpLoader.class);

    /**
     * Initializes the given cycled queue (JmsOperation specified above must be set). The cycled queue is
     * a message that is continuously sent to the same queue doing some action each time.
     *
     * @param queueBean   The queue to be initialized (specified as a bean name in the
     *                    context)
     * @param messageKey  The message key
     * @param headerPairs Pairs name/value for additional headers
     * @param text        Text inside the message (to understand what it is the
     *                    message for)
     */
    protected void initQueue(String queueBean, String messageKey, String text, Object... headerPairs) {

        Map<String, Object> hh = toMap(headerPairs);
        hh.put(BaseEventKeyStrategy.TYPE_KEY, messageKey);
        /*
         * Storing the instance ID to process the message linked with the
         * current instance only
         */
        hh.put(LoopBaseEventKeyStrategy.INSTANCE_HEADER, LoopBaseEventKeyStrategy.getInstanceID());

        Message<String> msg = new GenericMessage<String>(text, hh);

        JmsOperations jms = bean("jmsTemplate", JmsOperations.class);

        Destination queue = bean(queueBean, Destination.class);
        jms.convertAndSend(queue, msg);

        logger.info("Queue: {} initialized", messageKey);
    }

    /**
     * The map which stores information about the queues to be initialized
     */
    private final List<Object[]> queues = list();

    /**
     * Adds a queue for postponed initialization. The real initialization will be
     * available in the 'production' mode only.
     *
     * @param queueBean   The name of queue bean
     * @param messageKey  The key for message identification
     * @param headerPairs Pairs 'name'/'value' for additional headers
     * @param body        The message body
     */
    protected void addQueue(String queueBean, String messageKey, String body, Object... headerPairs) {
        queues.add(new Object[]{queueBean, messageKey, body, headerPairs});
    }

    /**
     * Sends all prepared messages to preliminary registered queues
     */
    protected void sendAll() {
        queues.forEach(a -> initQueue(a[0].toString(), a[1].toString(), a[2].toString(), (Object[]) a[3]));
        queues.clear();
    }

    @PostConstruct
    public void init() {

        logger.info("Context: {}, profiles: {}, production: {}", getCtx(), getProfiles(), isProdMode());

        if (isProdMode()) {
            sendAll();
        } else {
            logger.info("Don't initialize the cycled queues when not in production mode");
        }
    }
}
