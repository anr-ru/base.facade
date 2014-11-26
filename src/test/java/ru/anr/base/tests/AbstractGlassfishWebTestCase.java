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
package ru.anr.base.tests;

import java.io.IOException;

import org.glassfish.embeddable.GlassFishException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.slf4j.LoggerFactory;

/**
 * Abstract configuration for test case which uses Embedded Glassfish to deploy
 * application for local testing.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */
@Ignore
public class AbstractGlassfishWebTestCase extends BaseTestCase {

    /**
     * Logger
     */
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractGlassfishWebTestCase.class);

    /**
     * Static ref to Glassfish instance
     */
    protected static GlassfishLoader loader;

    /**
     * Initialization and starting of Embedded Glassfish instance
     * 
     * @throws GlassFishException
     *             on some configuration exception
     * @throws IOException
     *             If some files not found
     */
    @BeforeClass
    public static synchronized void initialize() throws GlassFishException, IOException {

        if (loader == null) {

            synchronized (AbstractGlassfishWebTestCase.class) {

                loader = new GlassfishLoader("facade");

                loader.initialize();
                loader.deployApps();
            }
        }
    }
}
