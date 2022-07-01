package ru.anr.base.facade.samples.domain;

import ru.anr.base.domain.api.models.RequestModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Some test model.
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 */
@XmlRootElement(name = "ping")
public class PingRequestModel extends RequestModel {

    private static final long serialVersionUID = -631702176715469501L;

    /**
     * Message value
     */
    private String message;

    /**
     * @return the message
     */
    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
