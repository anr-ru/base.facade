package ru.anr.base.facade.samples.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.anr.base.ApplicationException;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.APIException;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.facade.samples.domain.PingResponseModel;
import ru.anr.base.services.api.AbstractApiCommandStrategyImpl;
import ru.anr.base.services.api.ApiStrategy;

/**
 * Sample API strategy.
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 */
@ApiStrategy(id = "ping", version = "v1", model = PingRequestModel.class)
@Component("PingApiStrategy")
public class PingApiStrategy extends AbstractApiCommandStrategyImpl {

    private static final Logger logger = LoggerFactory.getLogger(PingApiStrategy.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseModel get(APICommand cmd) {

        logger.debug("Ping processed: {}", cmd.getContexts().get("id"));

        PingResponseModel rs = new PingResponseModel();
        rs.setMessage("response," + cmd.getContexts().get("id"));

        return rs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseModel post(APICommand cmd) {

        PingRequestModel m = (PingRequestModel) cmd.getRequest();

        logger.debug("Ping processed: {}, {}", cmd.getContexts().get("id"), m.getMessage());

        PingResponseModel rs = new PingResponseModel();
        rs.setMessage(m.getMessage() + "," + cmd.getContexts().get("id"));

        return rs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseModel delete(APICommand cmd) {

        throw APIException.withCode("Shit happend", 100);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object put(APICommand cmd) {
        throw new ApplicationException("Just runtime exception");
    }
}
