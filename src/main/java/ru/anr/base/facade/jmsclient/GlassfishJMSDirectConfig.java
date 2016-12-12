/**
 * 
 */
package ru.anr.base.facade.jmsclient;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sun.messaging.jmq.jmsclient.JMSXAQueueConnectionFactoryImpl;

import ru.anr.base.ApplicationException;

/**
 * A configuration for possibility to make a direct connection to a Glassfish
 * JMS server (without a JNDI).
 *
 *
 * @author Alexey Romanchuk
 * @created Dec 12, 2016
 *
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
            connectionProps.forEach((k, v) -> {
                addProperty(f, k.toString(), v.toString());
            });
        }
        return f;
    }

    /**
     * Adds a property to the given factory catching an exception if occurs
     * 
     * @param factory
     *            A factory
     * @param key
     *            The key of a property
     * @param value
     *            The value of a property
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
     * @param connectionProps
     *            the connectionProps to set
     */
    public void setConnectionProps(Properties connectionProps) {

        this.connectionProps = connectionProps;
    }

}
