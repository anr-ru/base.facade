/**
 * 
 */
package ru.anr.base.facade.samples.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.APIException;
import ru.anr.base.domain.api.MethodTypes;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.samples.domain.PingReponseModel;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.services.api.AbstractApiCommandStrategyImpl;
import ru.anr.base.services.api.ApiMethod;
import ru.anr.base.services.api.ApiStrategy;

/**
 * Sample API strategy.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 *
 */
@ApiStrategy(id = "ping", version = "v1", model = PingRequestModel.class)
@Component("PingApiStrategy")
public class PingApiStrategy extends AbstractApiCommandStrategyImpl {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(PingApiStrategy.class);

    /**
     * Get implementation
     * 
     * @param cmd
     *            Api command
     * @return response
     */
    @ApiMethod(MethodTypes.Get)
    public ResponseModel doGet(APICommand cmd) {

        logger.debug("Ping processed: {}", cmd.getContexts().get("id"));

        PingReponseModel rs = new PingReponseModel();
        rs.setMessage("response," + cmd.getContexts().get("id"));

        return rs;
    }

    /**
     * Get implementation
     * 
     * @param cmd
     *            Api command
     * @return response
     */
    @ApiMethod(MethodTypes.Post)
    public ResponseModel doPost(APICommand cmd) {

        PingRequestModel m = (PingRequestModel) cmd.getRequest();

        logger.debug("Ping processed: {}, {}", cmd.getContexts().get("id"), m.getMessage());

        PingReponseModel rs = new PingReponseModel();
        rs.setMessage(m.getMessage() + "," + cmd.getContexts().get("id"));

        return rs;
    }

    /**
     * Delete implementation which throws an exception
     * 
     * @param cmd
     *            Api command
     * @return response
     */
    @ApiMethod(MethodTypes.Delete)
    public ResponseModel doDelete(APICommand cmd) {

        throw new APIException("Shit happend", 100);
    }

}
