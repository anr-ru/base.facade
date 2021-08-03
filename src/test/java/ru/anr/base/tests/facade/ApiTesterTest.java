package ru.anr.base.tests.facade;

import org.junit.Assert;
import org.junit.Test;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.tests.APITester;

/**
 * Testing API via RESTClient
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 */

public class ApiTesterTest extends BaseWebTestCase {


    @Test
    public void apiTester() {

        APITester api = new APITester(this);

        RestClient client = new RestClient();
        try {
            api.apiDELETE(client, "/api/v1/delete", Object.class);
            Assert.fail();
        } catch (AssertionError ex) {
            Assert.assertEquals("Client Error (400) / {\"code\":100,\"message\":\"Shit happend\"}", ex.getMessage());
        }

        // 2. Server Error
        try {
            api.apiPUT(client, "/api/v1/ping/1", new PingRequestModel());
            Assert.fail();
        } catch (AssertionError ex) {
            Assert.assertEquals("Server Error (500) / {\"code\":1,\"message\":\"Just runtime exception\"}", ex.getMessage());
        }

    }

}
