package ru.anr.base.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.facade.samples.domain.PingResponseModel;
import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.tests.facade.BaseWebTestCase;

/**
 * Testing API via RESTClient
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 */

public class ApiTesterTest extends BaseWebTestCase {


    @Test
    public void apiTester() {

        RestClient client = new RestClient();
        try {
            APITester.apiDELETE(client, "/api/v1/delete", Object.class);
            Assertions.fail();
        } catch (AssertionError ex) {
            Assertions.assertEquals(
                    "HTTP Error (400 BAD_REQUEST - Bad Request) " +
                            "/ {\"code\":100,\"message\":\"Shit happened\"}", ex.getMessage());
        }

        // 2. Server Error
        try {
            APITester.apiPUT(client, "/api/v1/ping/1", new PingRequestModel());
            Assertions.fail();
        } catch (AssertionError ex) {
            Assertions.assertEquals("HTTP Error (500 INTERNAL_SERVER_ERROR - Internal Server Error) " +
                    "/ {\"code\":1,\"message\":\"Just runtime exception\"}", ex.getMessage());
        }

    }

    @Test
    public void patch() {
        RestClient client = new RestClient();
        PingRequestModel rq = new PingRequestModel();
        rq.data = "YYY";
        PingResponseModel rs = APITester.apiPATCH(client, "/api/v1/ping/PATCH", rq, PingResponseModel.class);
        Assertions.assertEquals("PATCHED: PATCH : YYY", rs.data);
    }
}
