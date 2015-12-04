/**
 * 
 */
package ru.anr.base.facade.ejb.api.responses;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsOperations;
import org.springframework.util.Assert;

import ru.anr.base.services.api.APICommandFactory;
import ru.anr.base.services.api.ApiConfig;

/**
 * The handler side configuration.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 29, 2015
 *
 */
@Configuration
@Import(ApiConfig.class)
public class AsyncAPIHandlerConfig {

    /**
     * The key name to distinguish the API JMS messages.
     */
    private String keyName;

    /**
     * Creates a bean to send responses and process error
     * 
     * @param factory
     *            The {@link APICommandFactory} which the bean depends on
     * @param jms
     *            The {@link JmsOperations}
     * @return The bean instance
     */
    @Bean
    public APIResponseManager apiResponseManager(APICommandFactory factory, JmsOperations jms) {

        return new APIResponseManagerImpl();
    }

    /**
     * Creates the strategy bean which is responsible for message processing
     * 
     * @param responses
     *            The response manager
     * @return The bean instance
     */
    @Bean(name = "AsyncAPIStrategy")
    public AsyncAPIStrategy asyncAPIStrategy(APIResponseManager responses) {

        Assert.notNull(keyName, "The unique key name is not specified");
        return new AsyncAPIStrategy(keyName);
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param keyName
     *            the keyName to set
     */
    public void setKeyName(String keyName) {

        this.keyName = keyName;
    }
}
