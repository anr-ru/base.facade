/**
 * 
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
import ru.anr.base.facade.ejb.api.AsyncAPIHeaders;
import ru.anr.base.services.BaseServiceImpl;
import ru.anr.base.services.api.APICommandFactory;

/**
 * 
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
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void error(APICommand request, Exception ex) {

        String queue = getQueue(request);
        if (queue != null) {

            APICommand rs = apis.error(request, ex, request.getRequest());
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
