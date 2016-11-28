/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A configuration for health beans in case of a simple Spring Boot application
 * which does not have EJBs.
 * 
 * This class must be put to a web spring context.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 *
 */
@Configuration
public class HealthCheckSimpleConfig {

    /**
     * Exports a controller for serving '/health' requests.
     * 
     * @return A bean instance
     */
    @Bean(name = "HealthController")
    public HealthCheckController health() {

        return new HealthCheckController();
    }

    /**
     * Exports a EJB proxy bean for accessing to the EJB-located spring context
     * 
     * @return A bean instance
     */
    @Bean(name = "health")
    public HealthCheck bean() {

        return new HealthCheckImpl();
    }
}
