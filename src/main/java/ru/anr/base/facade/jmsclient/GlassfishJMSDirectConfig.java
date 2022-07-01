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

import com.sun.messaging.jmq.jmsclient.JMSXAQueueConnectionFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.anr.base.ApplicationException;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.Properties;

/**
 * A configuration for possibility to make a direct connection to a Glassfish
 * JMS server (without a JNDI).
 *
 * @author Alexey Romanchuk
 * @created Dec 12, 2016
 */

@Configuration
public class GlassfishJMSDirectConfig {
    /**
     * Properties required to make a connection
     */
    private Properties connectionProps;

    /**
     * @return A bean instance
     */
    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {

        JMSXAQueueConnectionFactoryImpl f = new JMSXAQueueConnectionFactoryImpl();

        if (connectionProps != null) {
            connectionProps.forEach((k, v) -> addProperty(f, k.toString(), v.toString()));
        }
        return f;
    }

    /**
     * Adds a property to the given factory catching an exception if occurs
     *
     * @param factory A factory
     * @param key     The key of a property
     * @param value   The value of a property
     */
    private void addProperty(JMSXAQueueConnectionFactoryImpl factory, String key, String value) {

        try {
            factory.setProperty(key, value);
        } catch (JMSException ex) {
            throw new ApplicationException(ex);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    ///// getters/setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param connectionProps the connectionProps to set
     */
    public void setConnectionProps(Properties connectionProps) {
        this.connectionProps = connectionProps;
    }
}
