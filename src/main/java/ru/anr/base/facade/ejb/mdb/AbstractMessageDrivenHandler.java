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
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessagingMessageConverter;
import org.springframework.util.Assert;
import ru.anr.base.ApplicationException;
import ru.anr.base.facade.ejb.AbstractEJBServiceImpl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Base implementation for JMS Handler. Performs conversion from JMS message to
 * some domain object from Spring ({@link org.springframework.messaging.Message}
 * ).
 *
 * @author Alexey Romanchuk
 * @created Nov 17, 2014
 */

public abstract class AbstractMessageDrivenHandler extends AbstractEJBServiceImpl implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageDrivenHandler.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(Message message) {

        Assert.isTrue(message instanceof TextMessage, "TextMessage only");
        MessageConverter converter = new MessagingMessageConverter();

        try {

            @SuppressWarnings("unchecked")
            org.springframework.messaging.Message<String> msg =
                    (org.springframework.messaging.Message<String>) converter.fromMessage(message);

            logMessage(msg);
            onMessage(msg);

        } catch (JMSException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Processing a converted to spring format JMS message
     *
     * @param msg A message
     */
    protected abstract void onMessage(org.springframework.messaging.Message<String> msg);

    /**
     * Logging a message (this can be overriden if not all msg headers is
     * allowed to log in file, for example some PIN codes and passwords).
     *
     * @param msg Converted JMS message
     */
    protected void logMessage(org.springframework.messaging.Message<String> msg) {
        logger.debug("Received a JMS message: {}", msg);
    }
}
