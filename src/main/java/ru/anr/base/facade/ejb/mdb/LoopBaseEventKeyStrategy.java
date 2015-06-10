/**
 * 
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

            if (pauseMSec > 0) {
                sleep(pauseMSec);
            }
            jms.convertAndSend(queue, msg); // re-sending

        } else {
            Destination bean = bean(dropQueueBean, Destination.class);
            jms.convertAndSend(bean, msg);

            logger.info("Non-production mode, the message {} sent to {}", msg, dropQueueBean);
        }
        return msg;
    }

    /**
     * @param dropQueueBean
     *            the dropQueue to set
     */
    public void setDropQueueBean(String dropQueueBean) {

        this.dropQueueBean = dropQueueBean;
    }
}
