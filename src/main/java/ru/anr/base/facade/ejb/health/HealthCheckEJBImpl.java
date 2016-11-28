/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import ru.anr.base.facade.ejb.AbstractEJBServiceImpl;

/**
 * An implementation of {@link HealthCheck} service.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 *
 */
public class HealthCheckEJBImpl extends AbstractEJBServiceImpl implements HealthCheck {

    /**
     * {@inheritDoc}
     */
    @Override
    public String check(boolean fail) {

        if (fail) {
            throw new ServiceUnavailableException("BOOM", "");
        }
        /*
         * It's supposed that accessing to the spring context should throw an
         * exception if the context has not been previously loaded yet.
         */
        try {
            return "" + getCtx().getStartupDate();
        } catch (Exception ex) {
            throw new ServiceUnavailableException("Context accessing failure", ex.getMessage());
        }
    }
}
