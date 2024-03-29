/*
 * Copyright 2014-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.facade.ejb.health;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean;

/**
 * A configuration for health beans in the case of using in a EJB environment.
 * This class must be put to a spring context fo a web application.
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 */
@Configuration
public class HealthCheckJEEConfig {

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
     * @param jndiName the jndiName to set
     */
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }
}
