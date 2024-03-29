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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.anr.base.BaseParent;

import javax.jms.Destination;
import javax.jms.Queue;

/**
 * A test (mock) queue (Used in {@link TestJmsOperations})
 *
 * @author Alexey Romanchuk
 * @created Nov 22, 2014
 */

public class TestDestination extends BaseParent implements Destination, Queue {

    /**
     * Destination name
     */
    private final String name;

    /**
     * Constructor for test queue
     *
     * @param name Name of 'queue'
     */
    public TestDestination(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" + name + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getQueueName() {
        return name;
    }
}
