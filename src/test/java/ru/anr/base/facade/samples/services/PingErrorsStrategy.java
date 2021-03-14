package ru.anr.base.facade.samples.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.APIException;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.samples.domain.PingRequestModel;
import ru.anr.base.facade.samples.domain.SomeObject;
import ru.anr.base.services.api.AbstractApiCommandStrategyImpl;
import ru.anr.base.services.api.ApiStrategy;

/**
 * Sample API strategy.
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 */
@ApiStrategy(id = "ping.errors", version = "v1", model = PingRequestModel.class)
@Component("PingErrorsStrategy")
public class PingErrorsStrategy extends AbstractApiCommandStrategyImpl {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseModel get(APICommand cmd) {

        throw new BadCredentialsException("Wrong password or alike");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object put(APICommand cmd) {

        SomeObject o = new SomeObject();

        validate(o);
        return new ResponseModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseModel post(APICommand cmd) {

        throw new AccessDeniedException("Unable to access this ping");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseModel delete(APICommand cmd) {

        throw APIException.validation("shit.happened", "Shit happened");
    }

}
