/**
 * 
 */
package ru.anr.base.facade.jmsclient;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessagingMessageConverter;

/**
 * JMS Configurations. Defines a Spring {@link JmsTemplate} bean with required
 * settings.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */
@Configuration
public class JmsConfig {

    /**
     * timeout
     */
    private Long receiveTimeout = JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT;

    /**
     * Cached factory (wrapper for jms factory from JNDI). Expects a bean
     * 'jmsConnectionFactory' defined in current context.
     * 
     * @param connectionFactory
     *            JMS Connection factory
     * @return Bean instance
     */
    @Bean(name = "connectionFactory")
    @DependsOn("jmsConnectionFactory")
    public CachingConnectionFactory cachedFactory(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory) {

        CachingConnectionFactory f = new CachingConnectionFactory(connectionFactory);
        f.setSessionCacheSize(100);

        // Doesn't work with Glassfish client ??
        f.setReconnectOnException(false);
        return f;
    }

    /**
     * Constructing a JMS template bean
     * 
     * @param connectionFactory
     *            {@link ConnectionFactory}
     * @return Bean instance
     */
    @Bean(name = "jmsTemplate")
    @DependsOn("connectionFactory")
    public JmsTemplate template(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {

        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setMessageConverter(new MessagingMessageConverter());

        if (receiveTimeout != null) {
            template.setReceiveTimeout(receiveTimeout);
        }

        return template;
    }

    /**
     * @param receiveTimeout
     *            the receiveTimeout
     */
    public void setReceiveTimeout(Long receiveTimeout) {

        this.receiveTimeout = receiveTimeout;
    }

}
