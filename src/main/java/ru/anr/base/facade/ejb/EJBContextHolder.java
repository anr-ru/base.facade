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
package ru.anr.base.facade.ejb;

import org.springframework.context.ApplicationContext;
import ru.anr.base.BaseSpringParent;

import javax.inject.Inject;

/**
 * A holder of the Spring context and a single parent for all EJB beans that
 * require a Spring context.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 */
public abstract class EJBContextHolder extends BaseSpringParent {
    /**
     * Inject the context as a CDI bean
     */
    @Override
    @Inject
    public void setCtx(@SpringContext ApplicationContext context) {
        super.setCtx(context);
    }

    public ApplicationContext getCtx() {
        return ctx;
    }
}
