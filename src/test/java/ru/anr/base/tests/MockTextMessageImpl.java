package ru.anr.base.tests;

import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.Message;
import ru.anr.base.BaseParent;

import javax.jms.Destination;
import javax.jms.JMSException;
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
    public String getJMSMessageID() throws JMSException {
        return (String) this.message.getHeaders().get(JmsHeaders.MESSAGE_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSMessageID(String id) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.MESSAGE_ID, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSTimestamp() throws JMSException {
        return (long) nullSafe(0L, this.message.getHeaders().get(JmsHeaders.TIMESTAMP));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSTimestamp(long timestamp) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.TIMESTAMP, timestamp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
        return BaseParent.utf8(this.getJMSCorrelationID());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {
        this.setJMSCorrelationID(BaseParent.utf8(correlationID));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSCorrelationID(String correlationID) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.CORRELATION_ID, correlationID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJMSCorrelationID() throws JMSException {
        return (String) this.message.getHeaders().get(JmsHeaders.CORRELATION_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Destination getJMSReplyTo() throws JMSException {
        return (Destination) this.message.getHeaders().get(JmsHeaders.REPLY_TO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSReplyTo(Destination replyTo) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.REPLY_TO, replyTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Destination getJMSDestination() throws JMSException {
        return (Destination) this.message.getHeaders().get(JmsHeaders.DESTINATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDestination(Destination destination) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.DESTINATION, destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJMSDeliveryMode() throws JMSException {
        return (int) nullSafe(0, this.message.getHeaders().get(JmsHeaders.DELIVERY_MODE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDeliveryMode(int deliveryMode) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.DELIVERY_MODE, deliveryMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getJMSRedelivered() throws JMSException {
        return (boolean) nullSafe(false, this.message.getHeaders().get(JmsHeaders.REDELIVERED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSRedelivered(boolean redelivered) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.REDELIVERED, redelivered);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJMSType() throws JMSException {
        return (String) this.message.getHeaders().get(JmsHeaders.TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSType(String type) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.TYPE, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSExpiration() throws JMSException {
        return (long) nullSafe(0L, this.message.getHeaders().get(JmsHeaders.EXPIRATION));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSExpiration(long expiration) throws JMSException {
        this.message.getHeaders().put(JmsHeaders.EXPIRATION, expiration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSDeliveryTime() throws JMSException {
        return (long) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDeliveryTime(long deliveryTime) throws JMSException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJMSPriority() {
        return (int) nullSafe(0, this.message.getHeaders().get(JmsHeaders.PRIORITY));
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
    public long getLongProperty(String name) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloatProperty(String name) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleProperty(String name) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringProperty(String name) throws JMSException {
        return (String) this.message.getHeaders().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObjectProperty(String name) throws JMSException {
        return this.message.getHeaders().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<?> getPropertyNames() throws JMSException {
        return new Vector<>(this.message.getHeaders().keySet()).elements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBooleanProperty(String name, boolean value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setByteProperty(String name, byte value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShortProperty(String name, short value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIntProperty(String name, int value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLongProperty(String name, long value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloatProperty(String name, float value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDoubleProperty(String name, double value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStringProperty(String name, String value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObjectProperty(String name, Object value) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acknowledge() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearBody() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getBody(Class<T> c) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBodyAssignableTo(@SuppressWarnings("rawtypes") Class c) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(String string) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() throws JMSException {

        return message.getPayload();
    }
}
