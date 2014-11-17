/**
 * 
 */
package ru.anr.base.facade.ejb;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessagingMessageConverter;
import org.springframework.util.Assert;

import ru.anr.base.ApplicationException;

/**
 * Base implementation for JMS Handler. Performs conversion from JMS message to
 * some domain object from Spring ({@link org.springframework.messaging.Message}
 * ).
 *
 * @author Alexey Romanchuk
 * @created Nov 17, 2014
 *
 */

public abstract class AbstractMessageDrivenHandler extends AbstractEJBServiceImpl implements MessageListener {

    /**
     * Logger
     */
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

            logger.debug("Received a JMS message: {}", msg);
            onMessage(msg);

        } catch (JMSException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Processing a converted to spring format JMS message
     * 
     * @param msg
     *            A message
     */
    protected abstract void onMessage(org.springframework.messaging.Message<String> msg);

    /**
     * Logging a message (this can be overriden)
     * 
     * @param msg
     *            Converted JMS message
     */
    protected void logMessage(org.springframework.messaging.Message<String> msg) {

        logger.debug("Received a JMS message: {}", msg);
    }
}
