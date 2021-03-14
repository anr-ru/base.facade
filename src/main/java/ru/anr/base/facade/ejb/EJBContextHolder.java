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
package ru.anr.base.facade.ejb;

import org.springframework.context.ApplicationContext;
import ru.anr.base.BaseParent;

import javax.inject.Inject;

/**
 * A holder of Spring context (static reference). The idea is that @Startup EJB
 * loads necessary context and then stores it inside this class only once.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 */
public class EJBContextHolder extends BaseParent {

    /**
     * A reference to Spring context
     */
    @Inject
    private ApplicationContext ctx;

    // ////////////////////////// Accessing beans /////////////////////////////

    /**
     * Getting bean from context (a short-cut)
     *
     * @param name  Name of bean
     * @param clazz Bean class
     * @param <S>   Type of bean
     * @return Bean instance
     */
    protected <S> S bean(String name, Class<S> clazz) {

        return ctx.getBean(name, clazz);
    }

    /**
     * Getting bean from context (a short-cut)
     *
     * @param name Name of bean
     * @param <S>  Type of bean
     * @return Bean instance
     */
    @SuppressWarnings("unchecked")
    protected <S> S bean(String name) {

        return (S) ctx.getBean(name);
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @return the ctx
     */
    public ApplicationContext getCtx() {

        return ctx;
    }
}
