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

import org.springframework.jms.core.JmsOperations;

import javax.jms.Destination;
import java.util.Queue;

/**
 * An interface to work with an underlying mocked queue.
 *
 * @author Alexey Romanchuk
 * @created Apr 29, 2015
 */

public interface JmsTests extends JmsOperations {

    /**
     * Returns an internal mock-queue object by the specified destination.
     *
     * @param queue The queue
     * @param <S>   Type of a queue item
     * @return The queue object
     */
    <S> Queue<S> queue(Destination queue);

    /**
     * Cleans the specified queue (removes all items)
     *
     * @param queue The queue to clean
     */
    void clean(Destination queue);
}
