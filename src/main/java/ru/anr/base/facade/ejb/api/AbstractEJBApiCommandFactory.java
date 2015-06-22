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
package ru.anr.base.facade.ejb.api;

import org.springframework.beans.factory.annotation.Autowired;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.facade.ejb.AbstractEJBServiceImpl;
import ru.anr.base.services.api.APICommandFactory;

/**
 * EJB prototype for implementing API Command Factory bean. It works just like a
 * wrapper and such EJB implementation give us a possibility to work in JEE
 * container with container-based transaction support.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 *
 */
public abstract class AbstractEJBApiCommandFactory extends AbstractEJBServiceImpl implements APICommandFactory {

    /**
     * A reference to a spring bean. Bean implementing an interface
     * {@link APICommandFactory} should be defined in Spring context.
     */
    @Autowired
    private APICommandFactory factory;

    /**
     * {@inheritDoc}
     */
    @Override
    public APICommand process(APICommand cmd) {

        return factory.process(cmd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public APICommand error(APICommand cmd, Throwable ex) {

        return factory.error(cmd, ex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public APICommand error(Throwable ex) {

        return factory.error(ex);
    }
}
