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
package ru.anr.base.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Queue;

import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.jms.core.SessionCallback;
import org.springframework.util.Assert;

import ru.anr.base.BaseParent;

/**
 * A test implementation for {@link JmsOperations} to use instead of real JMS
 * template.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 22, 2014
 *
 */

public class TestJmsOperations extends BaseParent implements JmsOperations {

    /**
     * Embedded queue
     */
    private final Map<String, java.util.Queue<Object>> queueMap;

    /**
     * Stored default queue
     */
    private Destination defaultDestination;

    /**
     * Constructor
     */
    public TestJmsOperations() {

        this.queueMap = new HashMap<>();
    }

    /**
     * Getting stored queue (or creating a new one in storage)
     * 
     * @param destination
     *            Destination to use
     * @return Mock queue instance
     */
    private java.util.Queue<Object> getQueue(Object destination) {

        Assert.notNull(destination, "Destination is null");

        String key = destination.toString();
        java.util.Queue<Object> q = queueMap.get(key);

        if (q == null) {
            q = new ConcurrentLinkedQueue<>();
            queueMap.put(key, q);
        }
        return q;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T execute(SessionCallback<T> action) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T execute(ProducerCallback<T> action) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T execute(Destination destination, ProducerCallback<T> action) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T execute(String destinationName, ProducerCallback<T> action) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(MessageCreator messageCreator) {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Destination destination, MessageCreator messageCreator) {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(String destinationName, MessageCreator messageCreator) {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAndSend(Object message) {

        convertAndSend(defaultDestination, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAndSend(Destination destination, Object message) {

        Assert.notNull(destination);
        convertAndSend(destination.toString(), message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAndSend(String destinationName, Object message) {

        java.util.Queue<Object> q = getQueue(destinationName);
        q.add(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAndSend(Object message, MessagePostProcessor postProcessor) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAndSend(Destination destination, Object message, MessagePostProcessor postProcessor) {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAndSend(String destinationName, Object message, MessagePostProcessor postProcessor) {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receive() {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receive(Destination destination) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receive(String destinationName) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receiveSelected(String messageSelector) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receiveSelected(Destination destination, String messageSelector) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receiveSelected(String destinationName, String messageSelector) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object receiveAndConvert() {

        return receiveAndConvert(defaultDestination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object receiveAndConvert(Destination destination) {

        Assert.notNull(destination);
        return receiveAndConvert(destination.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object receiveAndConvert(String destinationName) {

        java.util.Queue<Object> q = getQueue(destinationName);
        return q.poll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object receiveSelectedAndConvert(String messageSelector) {

        return receiveAndConvert(defaultDestination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object receiveSelectedAndConvert(Destination destination, String messageSelector) {

        return receiveAndConvert(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object receiveSelectedAndConvert(String destinationName, String messageSelector) {

        return receiveAndConvert(destinationName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message sendAndReceive(MessageCreator messageCreator) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message sendAndReceive(Destination destination, MessageCreator messageCreator) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message sendAndReceive(String destinationName, MessageCreator messageCreator) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T browse(BrowserCallback<T> action) {

        Assert.notNull(defaultDestination);
        return (T) getQueue(defaultDestination.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T browse(Queue queue, BrowserCallback<T> action) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T browse(String queueName, BrowserCallback<T> action) {

        return (T) getQueue(queueName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T browseSelected(String messageSelector, BrowserCallback<T> action) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T browseSelected(Queue queue, String messageSelector, BrowserCallback<T> action) {

        throw new UnsupportedOperationException();
    }

    /**
     * @param defaultDestination
     *            the defaultDestination to set
     */
    public void setDefaultDestination(Destination defaultDestination) {

        this.defaultDestination = defaultDestination;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T browseSelected(String queueName, String messageSelector, BrowserCallback<T> action) {

        throw new UnsupportedOperationException();
    }
}
