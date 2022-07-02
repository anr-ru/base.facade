package ru.anr.base.facade.ejb.api;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.MethodTypes;
import ru.anr.base.facade.ejb.api.requests.AsyncAPIRequests;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.tests.JmsTests;
import ru.anr.base.tests.facade.BaseWebTestCase;

import javax.jms.Destination;
import java.util.List;
import java.util.Map;

/**
 * Tests of Asynchronous Messaging.
 *
 * @author Alexey Romanchuk
 * @created Nov 20, 2015
 */
public class AsyncAPIRequestsImplTest extends BaseWebTestCase {

    @Autowired
    @Qualifier("asyncApiService")
    private AsyncAPIRequests apis;

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
        Assertions.assertNotNull(msg);

        Assertions.assertEquals("async.ping", msg.getHeaders().get("API_STRATEGY_ID"));
        Assertions.assertEquals("v1", msg.getHeaders().get("API_VERSION"));
        Assertions.assertEquals("Get", msg.getHeaders().get("API_METHOD"));

        Assertions.assertEquals(id, msg.getHeaders().get("API_QUERY_ID"));

        Assertions.assertEquals(123, msg.getHeaders().get("XXX"));
        Assertions.assertEquals("zzz", msg.getHeaders().get("YYY"));
    }

    /**
     * Use case : receiving a message by its ID
     */
    @Test
    public void testReceiving() {

        jms.clean(responses);

        PingRequestModel m = new PingRequestModel();
        m.description = "self";

        Map<String, Object> hh = toMap("xxx", 123, "yyy", "zzz");
        hh.put(AsyncAPIHeaders.API_QUERY_ID.name(), "ID");
        hh.put(AsyncAPIHeaders.API_METHOD.name(), "Post");

        Message<String> msg = apis.toMessage(m, hh);
        jms.convertAndSend(responses, msg);

        APICommand cmd = apis.getResponse("ID", PingRequestModel.class);
        PingRequestModel r = cmd.getResponse();
        Assertions.assertEquals("self", r.description);

        // TypeReference
        List<PingRequestModel> list = list(m);
        msg = apis.toMessage(list, hh);

        jms.convertAndSend(responses, msg);

        TypeReference<List<PingRequestModel>> type = new TypeReference<>() {
        };

        cmd = apis.getResponse("ID", type);
        List<PingRequestModel> rs = cmd.getResponse();
        Assertions.assertEquals("self", rs.get(0).description);
    }
}
