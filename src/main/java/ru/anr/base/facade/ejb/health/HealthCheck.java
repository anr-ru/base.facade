/**
 * 
 */
package ru.anr.base.facade.ejb.health;

/**
 * A health check service. Any implementation should perform some deep checking
 * for availability on underlying infrastructure.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 *
 */

public interface HealthCheck {

    /**
     * A single function to use
     * 
     * @param fail
     *            true, if a intended exception is expected (for tests).
     * @return Some status text showing that the underlying infrastructure has
     *         started.
     */
    String check(boolean fail);
}
