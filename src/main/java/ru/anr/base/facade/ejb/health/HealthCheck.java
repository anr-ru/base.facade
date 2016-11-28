/**
 * 
 */
package ru.anr.base.facade.ejb.health;

/**
 * A health check service.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 *
 */

public interface HealthCheck {

    /**
     * The single function to use
     * 
     * @param fail
     *            true, if a intended exception is expected (for tests).
     * 
     * @return Some status text
     */
    String check(boolean fail);
}
