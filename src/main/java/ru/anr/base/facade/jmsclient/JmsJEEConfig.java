/*
 * Copyright 2014-2023 the original author or authors.
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessagingMessageConverter;

import javax.jms.ConnectionFactory;

/**
 * JMS Configurations for working in a JEE container. Defines a Spring
 * {@link JmsTemplate} bean with required settings (Simplifies spring bean
 * creation).
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 */
@Configuration
public class JmsJEEConfig {

    /**
     * Timeout, no wait mode by default
     */
    private Long receiveTimeout = JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT;

    /**
     * Constructing a JMS template bean
     *
     * @param connectionFactory {@link ConnectionFactory}
     * @return Bean instance
     */
    @Bean(name = "jmsTemplate")
    public JmsTemplate template(ConnectionFactory connectionFactory) {

        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setMessageConverter(new MessagingMessageConverter());

        if (receiveTimeout != null) {
            template.setReceiveTimeout(receiveTimeout);
        }
        return template;
    }

    ///////////////////////////////////////////////////////////////////////////
    ///// getters/setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param receiveTimeout the receiveTimeout
     */
    public void setReceiveTimeout(Long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

}
