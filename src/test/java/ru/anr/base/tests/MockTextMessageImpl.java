/**
 * 
 */
package ru.anr.base.tests;

import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.messaging.Message;

import ru.anr.base.BaseParent;

/**
 * A mock text message used in operations which require raw JMS Objects like
 * 'queueu browsing'.
 *
 *
 * @author Alexey Romanchuk
 * @created Jun 17, 2015
 *
 */

public class MockTextMessageImpl extends BaseParent implements TextMessage {

    /**
     * Embedded Spring message
     */
    private Message<String> message;

    /**
     * Constructor
     * 
     * @param message
     *            The message
     */
    public MockTextMessageImpl(Message<String> message) {

        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJMSMessageID() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSMessageID(String id) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSTimestamp() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSTimestamp(long timestamp) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getJMSCorrelationIDAsBytes() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSCorrelationID(String correlationID) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJMSCorrelationID() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Destination getJMSReplyTo() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSReplyTo(Destination replyTo) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Destination getJMSDestination() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDestination(Destination destination) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJMSDeliveryMode() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDeliveryMode(int deliveryMode) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getJMSRedelivered() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSRedelivered(boolean redelivered) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJMSType() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSType(String type) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSExpiration() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSExpiration(long expiration) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getJMSDeliveryTime() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSDeliveryTime(long deliveryTime) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJMSPriority() throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJMSPriority(int priority) throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearProperties() throws JMSException {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean propertyExists(String name) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanProperty(String name) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByteProperty(String name) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShortProperty(String name) throws JMSException {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntProperty(String name) throws JMSException {

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

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<?> getPropertyNames() throws JMSException {

        throw new UnsupportedOperationException();
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
