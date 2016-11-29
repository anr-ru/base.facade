/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import ru.anr.base.BaseSpringParent;

/**
 * A basic non-EJB implementation of {@link HealthCheck} service.
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

        return HealthCheckUtils.checkWork(fail, ctx);
    }
}
