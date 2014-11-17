/**
 * 
 */
package ru.anr.base.facade.ejb.api;

import org.springframework.beans.factory.annotation.Autowired;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.facade.ejb.AbstractEJBServiceImpl;
import ru.anr.base.services.api.APICommandFactory;

/**
 * EJB prototype for implementing API Command Factory bean.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 *
 */
public abstract class AbstractEJBApiCommandFactory extends AbstractEJBServiceImpl implements APICommandFactory {

    /**
     * A reference to a spring bean. Bean implementing an interface
     * {@link APICommandFactory} should be defined in Spring context.
     */
    @Autowired
    private APICommandFactory factory;

    /**
     * {@inheritDoc}
     */
    @Override
    public APICommand process(APICommand cmd) {

        return factory.process(cmd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public APICommand error(APICommand cmd, Exception ex) {

        return factory.error(cmd, ex);
    }

}
