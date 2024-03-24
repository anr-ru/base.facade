/*
 * Copyright 2014-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.facade.ejb.health;

import org.springframework.context.ApplicationContext;

/**
 * Internal implementation of a health check service.
 *
 * @author Alexey Romanchuk
 * @created Nov 29, 2016
 */

final class HealthCheckUtils {

    /**
     * A private Constructor
     */
    private HealthCheckUtils() {
        // empty
    }

    /**
     * The checking work. This implementation allows to generate an intended
     * {@link ServiceUnavailableException} that can be used for test reasons.
     *
     * @param fail    true, if we need a forced exception
     * @param context A spring context
     * @return Time passed from the start of the given context
     */
    static String checkWork(boolean fail, ApplicationContext context) {

        if (fail) {
            throw new ServiceUnavailableException("BOOM", "");
        }
        /*
         * It's supposed that accessing to the spring context should throw an
         * exception if the context has not been previously loaded yet.
         */
        try {
            return String.valueOf(context.getStartupDate());
        } catch (Exception ex) {
            throw new ServiceUnavailableException("Context accessing failure", ex.getMessage());
        }
    }
}
