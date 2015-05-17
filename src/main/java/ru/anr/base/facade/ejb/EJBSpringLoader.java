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

import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Singleton EJB, which loads spring context via {@link SpringEJBInterceptor}
 * intercepter.
 * 
 * Add the @Singleton and @Startup annotations in descendants.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 *
 */
@Interceptors(SpringEJBInterceptor.class)
public class EJBSpringLoader {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(EJBSpringLoader.class);

    /**
     * Reference to holder to avoid class unloading.
     */
    private final EJBContextHolder holder = new EJBContextHolder();

    /**
     * An attempt to inject context
     */
    @Autowired
    private ApplicationContext ctx;

    /**
     * Initialization
     */
    @PostConstruct
    public void init() {

        logger.info("Holder: {}, context loaded: {}", holder, ctx);
    }
}
