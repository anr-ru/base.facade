/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import ru.anr.base.BaseSpringParent;

/**
 * An implementation of {@link HealthCheck} service.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 *
 */
public class HealthCheckImpl extends BaseSpringParent implements HealthCheck {

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
            return "" + ctx.getStartupDate();
        } catch (Exception ex) {
            throw new ServiceUnavailableException("Context accessing failure", ex.getMessage());
        }
    }
}
