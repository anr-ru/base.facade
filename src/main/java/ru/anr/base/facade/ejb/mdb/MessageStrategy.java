/**
 * 
 */
package ru.anr.base.facade.ejb.mdb;

import org.springframework.messaging.Message;

import ru.anr.base.services.pattern.Strategy;

/**
 * Strategy for message processing.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */

public interface MessageStrategy extends Strategy<Message<String>> {

    /**
     * Do nothing
     */
}
