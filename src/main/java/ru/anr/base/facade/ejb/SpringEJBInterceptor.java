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

import org.springframework.beans.factory.BeanFactory;
//import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 * This interceptor performs initial context loading from resource with specific
 * predefined name.
 * <p>
 * To used these beans, please add a context file with the name ejb-context.xml.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 */

public class SpringEJBInterceptor {

    /**
     * {@inheritDoc}
     */
    //@Override
    protected BeanFactory getBeanFactory(Object target) {

        //if (EJBContextHolder.getCtx() == null) { // lazy loading
        /*
         * TODO: is there any way to specify this path smarter and more
         * configurable?
         */
        //EJBContextHolder.loadContext("classpath:/ejb-context.xml");
        //}
        //return EJBContextHolder.getCtx().getAutowireCapableBeanFactory();
        return null;
    }
}
