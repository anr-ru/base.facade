/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import ru.anr.base.facade.ejb.AbstractEJBServiceImpl;

/**
 * A EJB implementation of {@link HealthCheck} service. To include this service
 * you need to create a new EJB based on this class.
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

        return HealthCheckUtils.checkWork(fail, getCtx());
    }
}
