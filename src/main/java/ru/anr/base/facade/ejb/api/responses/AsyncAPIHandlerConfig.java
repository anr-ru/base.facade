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
package ru.anr.base.facade.ejb.api.responses;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsOperations;
import org.springframework.util.Assert;
import ru.anr.base.BaseParent;
import ru.anr.base.services.api.APICommandFactory;
import ru.anr.base.services.api.ApiConfig;

/**
 * The handler side configuration.
 *
 * @author Alexey Romanchuk
 * @created Nov 29, 2015
 */
@Configuration
@Import(ApiConfig.class)
public class AsyncAPIHandlerConfig {
    /**
     * The key name to distinguish the API JMS messages.
     */
    private String keyName;


    private String responseClassName = APIResponseManagerImpl.class.getName();

    /**
     * Creates a bean to send responses and process error
     *
     * @param factory The {@link APICommandFactory} which the bean depends on
     * @param jms     The {@link JmsOperations}
     * @return The bean instance
     */
    @Bean
    public APIResponseManager apiResponseManager(APICommandFactory factory, JmsOperations jms) throws ClassNotFoundException {
        return (APIResponseManager) BaseParent.inst(Class.forName(responseClassName), new Class<?>[]{});
    }

    /**
     * Creates the strategy bean which is responsible for message processing
     *
     * @return The bean instance
     */
    @Bean(name = "AsyncAPIStrategy")
    public AsyncAPIStrategy asyncAPIStrategy() {
        Assert.notNull(keyName, "The unique key name is not specified");
        return new AsyncAPIStrategy(keyName);
    }

    ///////////////////////////////////////////////////////////////////////////
    ///// getters/setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param keyName the keyName to set
     */
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public void setResponseClassName(String responseClassName) {
        this.responseClassName = responseClassName;
    }
}
