package ru.anr.base.facade.samples.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.facade.samples.domain.PingResponseModel;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;
import ru.anr.base.services.api.AbstractApiCommandStrategyImpl;
import ru.anr.base.services.api.ApiStrategy;

/**
 * Sample API strategy.
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 */
@ApiStrategy(id = "norollback", version = "v1", model = PingRequestModel.class)
@Component
public class NoRollbackStrategy extends AbstractApiCommandStrategyImpl {

    private static final Logger logger = LoggerFactory.getLogger(NoRollbackStrategy.class);

    /**
     * {@link MyDao}
     */
    @Autowired
    private MyDao dao;

    /**
     * {@link SampleService}
     */
    @Autowired
    private SampleService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseModel post(APICommand cmd) {

        PingRequestModel m = cmd.getRequest();

        Samples s = new Samples();
        s.setName(m.data);

        s = dao.save(s);

        try {
            service.doWork();
        } catch (BadCredentialsException ex) {
            logger.error("Caught an exception: {}", ex.getMessage());
        }

        PingResponseModel rs = new PingResponseModel();
        rs.data = s.getId().toString();

        return rs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseModel put(APICommand cmd) {

        PingRequestModel m = cmd.getRequest();

        Samples s = new Samples();
        s.setName(m.data);

        s = dao.save(s);

        try {
            service.doWorkNoRollback();
        } catch (BadCredentialsException ex) {
            logger.error("Caught an exception: {}", ex.getMessage());
        }

        PingResponseModel rs = new PingResponseModel();
        rs.data = s.getId().toString();

        return rs;
    }
}
