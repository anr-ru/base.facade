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

/**
 * A health check service. Any implementation should perform some deep checking
 * for availability on underlying infrastructure.
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 */

public interface HealthCheck {
    /**
     * A single function to use
     *
     * @param fail true, if a intended exception is expected (for tests).
     * @return Some status text showing that the underlying infrastructure has
     * started.
     */
    String check(boolean fail);
}
