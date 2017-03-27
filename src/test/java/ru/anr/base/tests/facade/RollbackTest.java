/**
 * 
 */
package ru.anr.base.tests.facade;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ru.anr.base.facade.samples.domain.PingReponseModel;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;
import ru.anr.base.tests.APITester;

/**
 * Tests for rollbacks.
 *
 * @author Alexey Romanchuk
 * @created Mar 27, 2017
 *
 */

public class RollbackTest extends BaseWebTestCase {

    /**
     * {@link MyDao}
     */
    @Autowired
    @Qualifier("mydao")
    private MyDao dao;

    /**
     * {@link APITester}
     */
    private APITester api = new APITester(this);

    /**
     * {@link RestClient}
     */
    private RestClient client = new RestClient();

    /**
     * Use case: there was caught an exception without specifying no rollback.
     * We've got the transaction was fully rolled back.
     */
    @Test
    public void pingPost() {

        PingRequestModel rq = new PingRequestModel();
        rq.setMessage("hello");

        PingReponseModel rs = api.apiPOST(client, "/api/v1/rollback", rq, PingReponseModel.class);

        Samples s = dao.findOne(parse(rs.getMessage(), Long.class));
        Assert.assertNull(s);
    }

    /**
     * Use case: there was caught an exception WITH specifying no rollback.
     * Nothing was rolled-back
     */
    @Test
    public void pingPut() {

        PingRequestModel rq = new PingRequestModel();
        rq.setMessage("hello");

        PingReponseModel rs = api.apiPUT(client, "/api/v1/norollback", rq, PingReponseModel.class);

        Samples s = dao.findOne(parse(rs.getMessage(), Long.class));
        Assert.assertNotNull(s);
    }
}
