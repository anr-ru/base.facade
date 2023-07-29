/*
 * Copyright 2014-2023 the original author or authors.
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
package ru.anr.base.facade.ejb.api.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import ru.anr.base.BaseSpringParent;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.MethodTypes;
import ru.anr.base.domain.api.models.RequestModel;
import ru.anr.base.facade.ejb.api.AsyncAPIHeaders;
import ru.anr.base.facade.ejb.api.responses.AsyncAPIStrategy;
import ru.anr.base.facade.ejb.mdb.BaseEventKeyStrategy;
import ru.anr.base.services.serializer.Serializer;

import javax.jms.Destination;
import java.util.Map;
import java.util.function.Function;

/**
 * An implementation for {@link AsyncAPIRequests}.
 *
 * @author Alexey Romanchuk
 * @created Nov 20, 2015
 */
@Validated
public class AsyncAPIRequestsImpl extends BaseSpringParent implements AsyncAPIRequests {

    private static final Logger logger = LoggerFactory.getLogger(AsyncAPIRequestsImpl.class);

    /**
     * A reference to the serializer (xml, json)
     */
    private final Serializer serializer;

    /**
     * JMS to send and receive messages
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
    private final String keyName;

    /**
     * The default constructor requires a serializer
     *
     * @param serializer The serializer (XML or JSON)
     * @param keyName    The name of the key used to distinguish the API messages
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
        return new GenericMessage<>(serializer.toStr(model), headers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String query(String id, String version, MethodTypes method, RequestModel model, Object... params) {
        return internalQuery(id, version, method, model, responseQueue, params);
    }

    /**
     * The sending procedure.
     *
     * @param id      The identifier of the strategy to use
     * @param version The version of the strategy
     * @param method  The method of the strategy
     * @param model   A model to use
     * @param queue   A response queue if required
     * @param params  Parameters of the query
     * @return The ID of the query
     */
    private String internalQuery(String id, String version, MethodTypes method, RequestModel model, String queue,
                                 Object... params) {

        Assert.notNull(requestQueue, "The request queue is not defined");

        Map<String, Object> hh = toMap(BaseEventKeyStrategy.TYPE_KEY, keyName);
        hh.put(AsyncAPIHeaders.API_STRATEGY_ID.name(), id);
        hh.put(AsyncAPIHeaders.API_VERSION.name(), version);
        hh.put(AsyncAPIHeaders.API_METHOD.name(), method.name());

        // If we have some authorization, let's add the principal name to determine more precisely
        // where and how to send the response.
        Authentication token = SecurityContextHolder.getContext().getAuthentication();
        if (token != null && token.isAuthenticated()) {
            hh.put(AsyncAPIHeaders.API_PRINCIPAL.name(), nullSafe(token.getPrincipal()));
        }

        hh.putAll(toMap(params));

        if (queue != null) {
            hh.put(AsyncAPIHeaders.API_RESPONSE_TO.name(), queue);
        }

        String queryId = guid();

        if (!hh.containsKey(AsyncAPIHeaders.API_QUERY_ID.name())) {
            hh.put(AsyncAPIHeaders.API_QUERY_ID.name(), queryId);
        } else {
            queryId = (String) hh.get(AsyncAPIHeaders.API_QUERY_ID.name());
        }

        Message<String> msg = toMessage(model, hh);

        String requests = (String) hh.get(AsyncAPIHeaders.API_REQUEST_TO.name());
        if (requests == null) {
            // Use the default
            requests = requestQueue;
        }
        Destination d = bean(requests);

        jms.convertAndSend(d, msg);

        logger.debug("The Async JMS API query {}/{} with ID = {} has been sent to {}", id, version, queryId, requestQueue);
        return queryId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String noResponseQuery(String id, String version, MethodTypes method, RequestModel model,
                                  Object... params) {
        return internalQuery(id, version, method, model, null, params);
    }

    /**
     * The template for the message selector
     */
    private static final String SELECTOR = "%s = '%s'";

    /**
     * {@inheritDoc}
     */
    @Override
    public APICommand getResponse(String queryId) {
        Assert.notNull(responseQueue, "The response queue is not defined");
        String selector = String.format(SELECTOR, AsyncAPIHeaders.API_QUERY_ID.name(), queryId);

        Destination d = bean(responseQueue);

        @SuppressWarnings("unchecked")
        Message<String> msg = (Message<String>) jms.receiveSelectedAndConvert(d, selector);
        return msg == null ? null : AsyncAPIStrategy.buildCommand(msg);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public APICommand getResponse(String queryId, Class<?> responseClass) {
        return internalResponse(queryId, (s) -> serializer.fromStr(s, responseClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public APICommand getResponse(String queryId, TypeReference<?> ref) {
        return internalResponse(queryId, (s) -> serializer.fromStr(s, ref));
    }

    private APICommand internalResponse(String queryId, Function<String, Object> callback) {
        APICommand cmd = getResponse(queryId);
        if (cmd != null) {
            logger.debug("Raw response: {}", cmd.getRawModel());
            if (cmd.getRawModel() != null) {
                cmd.setResponse(callback.apply(cmd.getRawModel()));
            }
        }
        return cmd;
    }


    ///////////////////////////////////////////////////////////////////////////
    ///// getters/setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param requestQueue the requestQueue to set
     */
    public void setRequestQueue(String requestQueue) {
        this.requestQueue = requestQueue;
    }

    /**
     * @param responseQueue the responseQueue to set
     */
    public void setResponseQueue(String responseQueue) {
        this.responseQueue = responseQueue;
    }
}
