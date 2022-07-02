/*
 * Copyright 2014-2022 the original author or authors.
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

import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.Message;
import ru.anr.base.BaseParent;

import javax.jms.Destination;
import javax.jms.TextMessage;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A mock text message used in operations which require raw JMS Objects like
 * 'queueu browsing'.
 *
 * @author Alexey Romanchuk
 * @created Jun 17, 2015
 */

public class MockTextMessageImpl extends BaseParent implements TextMessage {

    /**
     * Embedded Spring message
     */
    private final Message<String> message;

    /**
     * Constructor
     *
     * @param message The message
     */
    public MockTextMessageImpl(Message<String> message) {

        this.message = message;
    }

    /**
     * Returns the embedded message
     *
     * @return The message
     */
    public Message<String> getMessage() {

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJMSMessageID() {
        return (String) this.message.getHeaders().get(JmsHeaders.MESSAGE_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSMessageID(String id) {
        this.message.getHeaders().put(JmsHeaders.MESSAGE_ID, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSTimestamp() {
        return nullSafe(this.message.getHeaders().get(JmsHeaders.TIMESTAMP), v -> (long) v).orElse(0L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSTimestamp(long timestamp) {
        this.message.getHeaders().put(JmsHeaders.TIMESTAMP, timestamp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getJMSCorrelationIDAsBytes() {
        return BaseParent.utf8(this.getJMSCorrelationID());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSCorrelationIDAsBytes(byte[] correlationID) {
        this.setJMSCorrelationID(BaseParent.utf8(correlationID));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSCorrelationID(String correlationID) {
        this.message.getHeaders().put(JmsHeaders.CORRELATION_ID, correlationID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJMSCorrelationID() {
        return (String) this.message.getHeaders().get(JmsHeaders.CORRELATION_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Destination getJMSReplyTo() {
        return (Destination) this.message.getHeaders().get(JmsHeaders.REPLY_TO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSReplyTo(Destination replyTo) {
        this.message.getHeaders().put(JmsHeaders.REPLY_TO, replyTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Destination getJMSDestination() {
        return (Destination) this.message.getHeaders().get(JmsHeaders.DESTINATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDestination(Destination destination) {
        this.message.getHeaders().put(JmsHeaders.DESTINATION, destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJMSDeliveryMode() {
        return nullSafe(this.message.getHeaders().get(JmsHeaders.DELIVERY_MODE), v -> (int) v).orElse(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDeliveryMode(int deliveryMode) {
        this.message.getHeaders().put(JmsHeaders.DELIVERY_MODE, deliveryMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getJMSRedelivered() {
        return nullSafe(this.message.getHeaders().get(JmsHeaders.REDELIVERED), v -> (boolean) v).orElse(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSRedelivered(boolean redelivered) {
        this.message.getHeaders().put(JmsHeaders.REDELIVERED, redelivered);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJMSType() {
        return (String) this.message.getHeaders().get(JmsHeaders.TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSType(String type) {
        this.message.getHeaders().put(JmsHeaders.TYPE, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSExpiration() {
        return nullSafe(this.message.getHeaders().get(JmsHeaders.EXPIRATION), v -> (long) v).orElse(0L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSExpiration(long expiration) {
        this.message.getHeaders().put(JmsHeaders.EXPIRATION, expiration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSDeliveryTime() {
        return 0L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDeliveryTime(long deliveryTime) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJMSPriority() {
        return nullSafe(this.message.getHeaders().get(JmsHeaders.PRIORITY), v -> (int) v).orElse(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSPriority(int priority) {
        this.message.getHeaders().put(JmsHeaders.PRIORITY, priority);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearProperties() {
        this.message.getHeaders().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean propertyExists(String name) {
        return this.message.getHeaders().containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanProperty(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByteProperty(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShortProperty(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntProperty(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongProperty(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloatProperty(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleProperty(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringProperty(String name) {
        return (String) this.message.getHeaders().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObjectProperty(String name) {
        return this.message.getHeaders().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<?> getPropertyNames() {
        return new Vector<>(this.message.getHeaders().keySet()).elements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBooleanProperty(String name, boolean value) {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setByteProperty(String name, byte value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShortProperty(String name, short value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIntProperty(String name, int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLongProperty(String name, long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloatProperty(String name, float value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDoubleProperty(String name, double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStringProperty(String name, String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObjectProperty(String name, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acknowledge() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearBody() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getBody(Class<T> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBodyAssignableTo(Class c) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(String string) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return message.getPayload();
    }
}
