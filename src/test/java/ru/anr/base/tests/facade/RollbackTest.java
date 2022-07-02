package ru.anr.base.tests.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.facade.samples.domain.PingResponseModel;
import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;
import ru.anr.base.tests.APITester;

/**
 * Tests for rollbacks.
 *
 * @author Alexey Romanchuk
 * @created Mar 27, 2017
 */

public class RollbackTest extends BaseWebTestCase {

    @Autowired
    private MyDao dao;

    /**
     * {@link RestClient}
     */
    private final RestClient client = new RestClient();

    /**
     * Use case: there was caught an exception without specifying no rollback.
     * We've got the transaction was fully rolled back.
     */
    @Test
    public void pingPost() {

        PingRequestModel rq = new PingRequestModel();
        rq.setMessage("hello");

        PingResponseModel rs = APITester.apiPOST(client, "/api/v1/rollback", rq, PingResponseModel.class);

        Samples s = dao.find(Samples.class, parse(rs.getMessage(), Long.class));
        Assertions.assertNull(s);
    }

    /**
     * Use case: there was caught an exception WITH specifying no rollback.
     * Nothing was rolled-back
     */
    @Test
    public void pingPut() {

        PingRequestModel rq = new PingRequestModel();
        rq.setMessage("hello");

        PingResponseModel rs = APITester.apiPUT(client, "/api/v1/norollback", rq, PingResponseModel.class);

        Samples s = dao.find(Samples.class, parse(rs.getMessage(), Long.class));
        Assertions.assertNotNull(s);
    }
}
