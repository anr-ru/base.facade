/**
 * 
 */
package ru.anr.base.facade.tests;

import javax.jms.Destination;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ru.anr.base.BaseParent;

/**
 * A test (mock) queue.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 22, 2014
 *
 */

public class TestDestination extends BaseParent implements Destination {

    /**
     * Destination name
     */
    private String name;

    /**
     * 
     * Constructor for test queue
     * 
     * @param name
     *            Name of 'queue'
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
}
