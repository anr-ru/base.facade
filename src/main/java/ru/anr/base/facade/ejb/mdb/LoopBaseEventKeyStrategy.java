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

package ru.anr.base.facade.ejb.mdb;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.Message;

/**
 * A strategy which resends a message back to the queue but in test case it
 * sends the message to 'ResponsesQueue' to avoid cycling.
 *
 *
 * @author Alexey Romanchuk
 * @created May 2, 2015
 *
 */

public class LoopBaseEventKeyStrategy extends BaseEventKeyStrategy {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(LoopBaseEventKeyStrategy.class);

    /**
     * The identifier of the current instance
     */
    private static String instanceId = guid();

    /**
     * The name of the header
     */
    public static final String INSTANCE_HEADER = "INSTANCE_HEADER";

    /**
     * Returns the current instance identifier which has been set during the JVM
     * start-up.
     * 
     * @return The identifier value
     */
    public static String getInstanceID() {

        return instanceId;
    }

    /**
     * JMS template
     */
    @Autowired
    protected JmsOperations jms;

    /**
     * Response queue bean used in tests to send message to stop
     */
    private String dropQueueBean = "testQueue";

    /**
     * The method can be used to organize re-sending back to the original queue
     * 
     * @param msg
     *            A message to send
     * @param queue
     *            The queue
     * @param pauseMSec
     *            amount of milliseconds for pause. 0 means 'no pause'
     * @return The message again
     */
    protected Message<String> loop(Message<String> msg, Destination queue, long pauseMSec) {

        if (isProdMode()) {
            if (matchedIDs(msg)) { // Using the current only
                if (pauseMSec > 0) {
                    sleep(pauseMSec);
                }
                jms.convertAndSend(queue, msg); // re-sending
            } else {
                logger.info("Removing old message: {}", msg);
            }
        } else {
            Destination bean = bean(dropQueueBean, Destination.class);
            jms.convertAndSend(bean, msg);

            logger.info("Non-production mode, the message {} sent to {}", msg, dropQueueBean);
        }
        return msg;
    }

    /**
     * Performs checking whether the header ID matches the instance on or not
     * 
     * @param msg
     *            The current message to process
     * @return true, if the header value is the same of the instance ID
     */
    protected boolean matchedIDs(Message<String> msg) {

        String value = (String) msg.getHeaders().get(LoopBaseEventKeyStrategy.INSTANCE_HEADER);

        logger.trace("Comparing the identifiers: {} vs {}", getInstanceID(), value);
        return safeEquals(getInstanceID(), value);
    }

    /**
     * @param dropQueueBean
     *            the dropQueue to set
     */
    public void setDropQueueBean(String dropQueueBean) {

        this.dropQueueBean = dropQueueBean;
    }
}
