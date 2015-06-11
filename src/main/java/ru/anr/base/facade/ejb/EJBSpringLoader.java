/*
 * Copyright 2014 the original author or authors.
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
package ru.anr.base.facade.ejb;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;
import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import ru.anr.base.BaseSpringParent;
import ru.anr.base.facade.ejb.mdb.BaseEventKeyStrategy;

/**
 * Singleton EJB, which loads spring context via {@link SpringEJBInterceptor}
 * intercepter.
 * 
 * Add the @Singleton and @Startup annotations in descendants.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 *
 */
@Interceptors(SpringEJBInterceptor.class)
public class EJBSpringLoader extends BaseSpringParent {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(EJBSpringLoader.class);

    /**
     * Reference to holder to avoid class unloading.
     */
    private final EJBContextHolder holder = new EJBContextHolder();

    /**
     * Reference to {@link JmsOperations}
     */
    @Autowired(required = false)
    private JmsOperations jms;

    /**
     * Initialization of a queue ( JmsOperation specified above must be set).
     * 
     * @param queueBean
     *            The queue to be initialized ( specified as a bean in the
     *            context)
     * @param messageKey
     *            Message key
     * @param headerPairs
     *            Pairs name/value for additional headers
     * @param text
     *            Text inside of the message (to understand what it is the
     *            message for)
     */
    protected void initQueue(String queueBean, String messageKey, String text, Object... headerPairs) {

        Map<String, Object> hh = toMap(headerPairs);
        hh.put(BaseEventKeyStrategy.TYPE_KEY, messageKey);

        Message<String> msg = new GenericMessage<String>(text, hh);

        Destination queue = bean(queueBean, Destination.class);
        jms.convertAndSend(queue, msg);

        logger.info("Queue: {} initialized", messageKey);
    }

    /**
     * The map which stores information about the queues to be initialized
     */
    private Map<String, Object[]> queueMap = toMap();

    /**
     * Adding queues for further initialization. Real initialization will be
     * available in 'production' mode only.
     * 
     * @param queueBean
     *            The name of queue bean
     * @param messageKey
     *            The key for message identification
     * @param headerPairs
     *            Pairs 'name'/'value' for additional headers
     * @param body
     *            Message body
     */
    protected void addQueue(String queueBean, String messageKey, String body, Object... headerPairs) {

        queueMap.put(queueBean, new Object[]{ messageKey, body, headerPairs });
    }

    /**
     * Initialization
     */
    @PostConstruct
    public void init() {

        logger.info("Holder: {}, profiles: {}", holder, getProfiles());

        if (isProdMode()) {
            queueMap.forEach((q, a) -> {
                initQueue(q.toString(), a[0].toString(), a[1].toString(), (Object[]) a[2]);
            });
        } else {
            logger.info("Don't initialize the cycled queues when not in production mode");
        }
    }
}
