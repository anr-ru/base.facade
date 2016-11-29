/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import org.springframework.context.ApplicationContext;

/**
 * Internal implementation of a health check service.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 29, 2016
 *
 */

final class HealthCheckUtils {

    /**
     * A private Constructor
     */
    private HealthCheckUtils() {
        // empty
    }

    /**
     * The checking work. This implementation allows to generate an intended
     * {@link ServiceUnavailableException} that can be used for test reasons.
     * 
     * @param fail
     *            true, if we need a forced exception
     * @param context
     *            A spring context
     * @return Time passed from the start of the given context
     */
    static String checkWork(boolean fail, ApplicationContext context) {

        if (fail) {
            throw new ServiceUnavailableException("BOOM", "");
        }
        /*
         * It's supposed that accessing to the spring context should throw an
         * exception if the context has not been previously loaded yet.
         */
        try {
            return "" + context.getStartupDate();
        } catch (Exception ex) {
            throw new ServiceUnavailableException("Context accessing failure", ex.getMessage());
        }
    }
}
