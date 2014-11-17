/**
 * 
 */
package ru.anr.base.facade.samples.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import ru.anr.base.domain.api.models.ResponseModel;

/**
 * Test response model.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 *
 */
@XmlRootElement(name = "pong")
public class PingReponseModel extends ResponseModel {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = -2838873002841632068L;

    /**
     * Some message
     */
    private String message;

    /**
     * @return the message
     */
    @XmlAttribute(name = "message")
    public String getMessage() {

        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {

        this.message = message;
    }
}
