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
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * CDI loaders for spring context. This is a single endpoint for producing
 * the main spring context used in the app.
 *
 * @author Alexey Romanchuk
 * @created Mar 18, 2021
 */
@ApplicationScoped
@Startup
@Singleton(name = "CDISpringLoader")
public class CDISpringLoader {

    @Produces
    @SpringContext
    @ApplicationScoped
    public ApplicationContext getApplicationContext() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:/ejb-context.xml");
        context.registerShutdownHook();
        return context;
    }
}
