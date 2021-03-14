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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import ru.anr.base.BaseSpringParent;
import ru.anr.base.facade.ejb.mdb.BaseEventKeyStrategy;
import ru.anr.base.facade.ejb.mdb.LoopBaseEventKeyStrategy;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.jms.Destination;
import java.util.List;
import java.util.Map;

/**
 * A singleton EJB which loads spring context via {@link SpringEJBInterceptor}
 * intercepter.
 * <p>
 * Add the @Singleton and @Startup annotations in descendants.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 */
public class EJBSpringLoader extends BaseSpringParent {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(EJBSpringLoader.class);

    /**
     * The main entry point for loading Spring context
     */
    @Produces
    public ApplicationContext getApplicationContext() {

        AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:/ejb-context.xml");
        context.registerShutdownHook();
        return context;
    }

    /**
     * Reference to {@link JmsOperations} if it is used
     */
    @Autowired(required = false)
    private JmsOperations jms;

    /**
     * Initialization of a queue ( JmsOperation specified above must be set).
     *
     * @param queueBean   The queue to be initialized ( specified as a bean in the
     *                    context)
     * @param messageKey  Message key
     * @param headerPairs Pairs name/value for additional headers
     * @param text        Text inside of the message (to understand what it is the
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

        Destination queue = bean(queueBean, Destination.class);
        jms.convertAndSend(queue, msg);

        logger.info("Queue: {} initialized", messageKey);
    }

    /**
     * The map which stores information about the queues to be initialized
     */
    private final List<Object[]> queues = list();

    /**
     * Adding queues for further initialization. Real initialization will be
     * available in 'production' mode only.
     *
     * @param queueBean   The name of queue bean
     * @param messageKey  The key for message identification
     * @param headerPairs Pairs 'name'/'value' for additional headers
     * @param body        Message body
     */
    protected void addQueue(String queueBean, String messageKey, String body, Object... headerPairs) {

        queues.add(new Object[]{queueBean, messageKey, body, headerPairs});
    }

    /**
     * Performs sending to all preliminary registered queues
     */
    protected void sendAll() {

        queues.forEach(a -> {
            initQueue(a[0].toString(), a[1].toString(), a[2].toString(), (Object[]) a[3]);
        });
        queues.clear();
    }

    @Inject
    private ApplicationContext context;

    /**
     * Initialization
     */
    @PostConstruct
    public void init() {

        logger.info("Context: {}, profiles: {}", context, getProfiles());

        if (isProdMode()) {
            sendAll();
        } else {
            logger.info("Don't initialize the cycled queues when not in production mode");
        }
    }
}
