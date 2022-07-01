package ru.anr.base.facade.samples.domain;

import javax.validation.constraints.NotNull;

/**
 * Description ...
 *
 * @author Alexey Romanchuk
 * @created Jun 22, 2015
 */

public class SomeObject {

    /**
     * The test property
     */
    private String property;

    /**
     * @return the property
     */
    @NotNull
    public String getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }

}
