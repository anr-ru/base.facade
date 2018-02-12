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

package ru.anr.base.facade.ejb.api.responses;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.ejb.api.AsyncAPIHeaders;
import ru.anr.base.services.BaseServiceImpl;
import ru.anr.base.services.api.APICommandFactory;

/**
 * An implementation of {@link APIResponseManager}.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2015
 *
 */
public class APIResponseManagerImpl extends BaseServiceImpl implements APIResponseManager {

    /**
     * {@link APICommandFactory}
     */
    @Autowired
    private APICommandFactory apis;

    /**
     * JMS template
     */
    @Autowired
    private JmsOperations jms;

    /**
     * {@inheritDoc}
     */
    @Override
    public void respond(APICommand request, APICommand response) {

        String queue = getQueue(request);
        if (queue != null) {
            sendResponse(queue, response.getRawModel(), request);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * Very important: here we use a new transaction to guarantee sending the
     * error message back in spite of rolling back of the original transaction
     * due to the exception.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void error(APICommand request, Exception ex) {

        String queue = getQueue(request);
        if (queue != null) {

            ResponseModel m = request.getRequest() == null ? new ResponseModel() : request.getRequest();

            APICommand rs = apis.error(request, ex, m);
            sendResponse(queue, rs.getRawModel(), request);
        }
    }

    /**
     * Extracts a queue to send the response
     * 
     * @param cmd
     *            The original command
     * @return The name of the queue
     */
    private String getQueue(APICommand cmd) {

        return (String) cmd.getContexts().get(AsyncAPIHeaders.API_RESPONSE_TO.name());
    }

    /**
     * Performs sending the response as a message
     * 
     * @param queue
     *            The queue to respond
     * @param body
     *            The body of the message
     * @param request
     *            The original request
     */
    private void sendResponse(String queue, String body, APICommand request) {

        Destination d = bean(queue);

        Map<String, Object> hh = new HashMap<String, Object>(request.getContexts());
        hh.remove(AsyncAPIHeaders.API_RESPONSE_TO.name());

        jms.convertAndSend(d, new GenericMessage<String>(body, hh));
    }
}
