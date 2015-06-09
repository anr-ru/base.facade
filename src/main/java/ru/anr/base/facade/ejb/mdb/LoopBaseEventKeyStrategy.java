/**
 * 
 */
package ru.anr.base.facade.ejb.mdb;

import javax.jms.Destination;

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
     * JMS template
     */
    @Autowired
    protected JmsOperations jms;

    /**
     * Response queue used in tests to send message to stop
     */
    private Destination dropQueue;

    /**
     * The method can be used to organize re-sending back to the original queue
     * 
     * @param msg
     *            A message to send
     * @param queue
     *            The queue
     * @param pause
     *            true if a 'pause' required before starting a new cycle
     * @return The message again
     */
    protected Message<String> loop(Message<String> msg, Destination queue, boolean pause) {

        if (msg.getHeaders().containsKey("TestMode")) {
            jms.convertAndSend(dropQueue, msg);
        } else {
            if (pause) {
                sleep(3000);
            }
            jms.convertAndSend(queue, msg); // re-sending
        }
        return msg;
    }

    /**
     * @param dropQueue
     *            the dropQueue to set
     */
    public void setDropQueue(Destination dropQueue) {

        this.dropQueue = dropQueue;
    }
}
