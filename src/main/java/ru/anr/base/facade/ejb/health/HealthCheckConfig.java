/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean;

/**
 * A configuration for health beans.
 * 
 * This class must be put to a web spring context.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 *
 */
@Configuration
public class HealthCheckConfig {

    /**
     * The JNDI name of a health service
     */
    private String jndiName;

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
    public LocalStatelessSessionProxyFactoryBean bean() {

        LocalStatelessSessionProxyFactoryBean bean = new LocalStatelessSessionProxyFactoryBean();

        bean.setJndiName(jndiName);
        bean.setBusinessInterface(HealthCheck.class);

        return bean;
    }

    ///////////////////////////////////////////////////////////////////////////
    ///// getters/setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param jndiName
     *            the jndiName to set
     */
    public void setJndiName(String jndiName) {

        this.jndiName = jndiName;
    }
}
