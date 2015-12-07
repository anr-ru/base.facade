/**
 * 
 */
package ru.anr.base.facade.ejb.api.requests;

import java.util.Map;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import ru.anr.base.BaseSpringParent;
import ru.anr.base.domain.api.MethodTypes;
import ru.anr.base.domain.api.models.RequestModel;
import ru.anr.base.facade.ejb.api.AsyncAPIHeaders;
import ru.anr.base.facade.ejb.mdb.BaseEventKeyStrategy;
import ru.anr.base.services.serializer.Serializer;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * The implementation for {@link AsyncAPIRequests}.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 20, 2015
 *
 */
@Validated
public class AsyncAPIRequestsImpl extends BaseSpringParent implements AsyncAPIRequests {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(AsyncAPIRequestsImpl.class);

    /**
     * A reference to the serializer (xml, json)
     */
    private final Serializer serializer;

    /**
     * Jms to send and receive messages
     */
    @Autowired(required = false)
    private JmsOperations jms;

    /**
     * The queue used for sending requests
     */
    private String requestQueue;

    /**
     * The queue used for receiving responses
     */
    private String responseQueue;

    /**
     * The name of the key
     */
    private String keyName;

    /**
     * The default constructor requires a serializer
     * 
     * @param serializer
     *            The serializer (XML or JSON)
     * @param keyName
     *            The name of the key used to distinguish the API messages
     */
    public AsyncAPIRequestsImpl(Serializer serializer, String keyName) {

        super();
        this.serializer = serializer;
        this.keyName = keyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> S toModel(Message<String> message, Class<S> clazz) {

        return serializer.fromStr(message.getPayload(), clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message<String> toMessage(Object model, Map<String, Object> headers) {

        return new GenericMessage<String>(serializer.toStr(model), headers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String query(String id, String version, MethodTypes method, RequestModel model, Object... params) {

        Assert.notNull(requestQueue, "The request queue is not defined");

        Map<String, Object> hh = toMap(BaseEventKeyStrategy.TYPE_KEY, keyName);
        hh.put(AsyncAPIHeaders.API_STRATEGY_ID.name(), id);
        hh.put(AsyncAPIHeaders.API_VERSION.name(), version);
        hh.put(AsyncAPIHeaders.API_METHOD.name(), method.name());
        hh.putAll(toMap(params));

        if (responseQueue != null) {
            hh.put(AsyncAPIHeaders.API_RESPONSE_TO.name(), responseQueue);
        }

        String queryId = guid();

        if (!hh.containsKey(AsyncAPIHeaders.API_QUERY_ID.name())) {
            hh.put(AsyncAPIHeaders.API_QUERY_ID.name(), queryId);
        } else {
            queryId = (String) hh.get(AsyncAPIHeaders.API_QUERY_ID.name());
        }

        Message<String> msg = toMessage(model, hh);

        Destination d = bean(requestQueue);

        jms.convertAndSend(d, msg);

        logger.info("The Async JMS API query with ID = {} has been sent to {}", queryId, requestQueue);
        return queryId;
    }

    /**
     * The template for the message selector
     */
    private static final String SELECTOR = "%s = '%s'";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponse(String queryId) {

        Assert.notNull(responseQueue, "The response queue is not defined");

        String selector = String.format(SELECTOR, AsyncAPIHeaders.API_QUERY_ID.name(), queryId);

        Destination d = bean(responseQueue);

        @SuppressWarnings("unchecked")
        Message<String> msg = (Message<String>) jms.receiveSelectedAndConvert(d, selector);
        return (msg == null) ? null : msg.getPayload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> S getResponse(String queryId, Class<S> responseClass) {

        String s = getResponse(queryId);
        logger.debug("Raw response: {}", s);
        return (s == null) ? null : serializer.fromStr(s, responseClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> S getResponse(String queryId, TypeReference<S> ref) {

        String s = getResponse(queryId);
        logger.debug("Raw response: {}", s);
        return (s == null) ? null : serializer.fromStr(s, ref);
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param requestQueue
     *            the requestQueue to set
     */
    public void setRequestQueue(String requestQueue) {

        this.requestQueue = requestQueue;
    }

    /**
     * @param responseQueue
     *            the responseQueue to set
     */
    public void setResponseQueue(String responseQueue) {

        this.responseQueue = responseQueue;
    }

}
