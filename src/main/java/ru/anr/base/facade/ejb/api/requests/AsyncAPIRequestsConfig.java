/**
 * 
 */
package ru.anr.base.facade.ejb.api.requests;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

import ru.anr.base.services.serializer.SerializationConfig;
import ru.anr.base.services.serializer.Serializer;

/**
 * The configuration to create {@link AsyncAPIRequests}.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2015
 *
 */
@Configuration
@Import(SerializationConfig.class)
public class AsyncAPIRequestsConfig {

    /**
     * The queue for requests
     */
    private String requestQueue;

    /**
     * The queue for responses
     */
    private String responseQueue;

    /**
     * The name of the key used to distinguish the API messages
     */
    private String keyName;

    /**
     * Creates a bean {@link AsyncAPIRequests}.
     * 
     * @param json
     *            JSON Serializer which the bean depends on
     * @return An instance of the bean
     */
    @Bean(name = "asyncApiService")
    public AsyncAPIRequests asyncApiService(@Qualifier("jsonSerializer") Serializer json) {

        Assert.notNull(requestQueue, "The request qiueue is not specified");
        Assert.notNull(keyName, "The name of distinct key is not specified");

        AsyncAPIRequestsImpl bean = new AsyncAPIRequestsImpl(json, keyName);
        bean.setRequestQueue(requestQueue);
        bean.setResponseQueue(responseQueue);

        return bean;
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param requestQueue
     *            the requestQueue to set
     */
    public void setRequestQueue(String requestQueue) {

        this.requestQueue = requestQueue;
    }

    /**
     * @param keyName
     *            the keyName to set
     */
    public void setKeyName(String keyName) {

        this.keyName = keyName;
    }

    /**
     * @param responseQueue
     *            the responseQueue to set
     */
    public void setResponseQueue(String responseQueue) {

        this.responseQueue = responseQueue;
    }
}
