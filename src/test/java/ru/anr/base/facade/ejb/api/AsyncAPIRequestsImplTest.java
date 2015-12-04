/**
 * 
 */
package ru.anr.base.facade.ejb.api;

import java.util.List;
import java.util.Map;

import javax.jms.Destination;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;

import ru.anr.base.domain.api.MethodTypes;
import ru.anr.base.facade.ejb.api.requests.AsyncAPIRequests;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.tests.JmsTests;
import ru.anr.base.tests.facade.BaseWebTestCase;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Description ...
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 20, 2015
 *
 */

public class AsyncAPIRequestsImplTest extends BaseWebTestCase {

    /**
     * {@link AsyncAPIRequests}
     */
    @Autowired
    private AsyncAPIRequests apis;

    /**
     * {@link JmsTests} defined for local tests
     */
    @Autowired
    private JmsTests jms;

    /**
     * For requests
     */
    @Autowired
    @Qualifier("queue")
    private Destination requests;

    /**
     * For responses
     */
    @Autowired
    @Qualifier("queue")
    private Destination responses;

    /**
     * Use case : sending a message, retrieving the query ID
     */
    @Test
    public void testSending() {

        jms.clean(requests);

        PingRequestModel m = new PingRequestModel();

        String id = apis.query("async.ping", "v1", MethodTypes.Get, m, "XXX", 123, "YYY", "zzz");

        @SuppressWarnings("unchecked")
        Message<String> msg = (Message<String>) jms.receiveAndConvert(requests);
        Assert.assertNotNull(msg);

        Assert.assertEquals("async.ping", msg.getHeaders().get("API_STRATEGY_ID"));
        Assert.assertEquals("v1", msg.getHeaders().get("API_VERSION"));
        Assert.assertEquals("Get", msg.getHeaders().get("API_METHOD"));

        Assert.assertEquals(id, msg.getHeaders().get("API_QUERY_ID"));

        Assert.assertEquals(123, msg.getHeaders().get("XXX"));
        Assert.assertEquals("zzz", msg.getHeaders().get("YYY"));
    }

    /**
     * Use case : receiving a message by its ID
     */
    @Test
    public void testReceiving() {

        jms.clean(responses);

        PingRequestModel m = new PingRequestModel();
        m.setDescription("self");

        Map<String, Object> hh = toMap("xxx", 123, "yyy", "zzz");
        hh.put(AsyncAPIHeaders.API_QUERY_ID.name(), "ID");

        Message<String> msg = apis.toMessage(m, hh);
        jms.convertAndSend(responses, msg);

        PingRequestModel r = apis.getResponse("ID", PingRequestModel.class);
        Assert.assertEquals("self", r.getDescription());

        // TypeReference
        List<PingRequestModel> list = list(m);
        msg = apis.toMessage(list, hh);

        jms.convertAndSend(responses, msg);

        TypeReference<List<PingRequestModel>> type = new TypeReference<List<PingRequestModel>>() {
        };

        List<PingRequestModel> rs = apis.getResponse("ID", type);
        Assert.assertEquals("self", rs.get(0).getDescription());
    }
}
