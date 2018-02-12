/*
 * Copyright 2014 the original author or authors.
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
