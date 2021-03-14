package ru.anr.base.tests;

import org.springframework.jms.core.JmsOperations;

import javax.jms.Destination;
import java.util.Queue;

/**
 * An interface to work with an underlying mock queue.
 *
 * @author Alexey Romanchuk
 * @created Apr 29, 2015
 */

public interface JmsTests extends JmsOperations {

    /**
     * Returns an internal mock-queue object by the specified destination.
     *
     * @param queue The queue
     * @param <S>   Type of a queue item
     * @return The queue object
     */
    <S> Queue<S> queue(Destination queue);

    /**
     * Cleans the specified queue (removes all items)
     *
     * @param queue The queue to clean
     */
    void clean(Destination queue);
}
