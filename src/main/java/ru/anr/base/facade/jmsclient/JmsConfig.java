/*
 * Copyright 2014-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.facade.jmsclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * JMS Configurations. Defines a Spring {@link JmsTemplate} bean with required
 * settings (Simplifies spring bean creation).
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 */
@Configuration
public class JmsConfig extends JmsJEEConfig {

    /**
     * Cached factory (wrapper for jms factory from JNDI). Expects a bean
     * 'jmsConnectionFactory' defined in current context.
     *
     * @param connectionFactory JMS Connection factory
     * @return Bean instance
     */
    @Bean(name = "connectionFactory")
    @DependsOn("jmsConnectionFactory")
    public SingleConnectionFactory cachedFactory(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory) {

        SingleConnectionFactory f = new SingleConnectionFactory(connectionFactory);

        // Doesn't work with a Glassfish client ??
        f.setReconnectOnException(false);
        return f;
    }

    /**
     * Constructing a JMS template bean
     *
     * @param connectionFactory {@link ConnectionFactory}
     * @return Bean instance
     */
    @Override
    @Bean(name = "jmsTemplate")
    @DependsOn("connectionFactory")
    public JmsTemplate template(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        return super.template(connectionFactory);
    }
}
