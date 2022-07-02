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
package ru.anr.base.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * An abstract configuration for test cases that use Embedded Glassfish to deploy
 * application for local testing.
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 */
@Disabled
public class AbstractGlassfishWebTestCase extends BaseTestCase {
    
    protected static final Logger logger = LoggerFactory.getLogger(AbstractGlassfishWebTestCase.class);

    /**
     * Static ref to Glassfish instance
     */
    protected static GlassfishLoader loader;

    /**
     * Initialization and starting of Embedded Glassfish instance
     */
    @BeforeAll
    public static synchronized void initialize() {

        if (loader == null) {

            synchronized (AbstractGlassfishWebTestCase.class) {
                Locale.setDefault(new Locale("en", "US"));
                loader = new GlassfishLoader("facade");

                loader.initialize();
                loader.deployApps();
            }
        }
    }
}
