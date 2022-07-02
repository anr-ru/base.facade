package ru.anr.base.facade.samples.domain;

import ru.anr.base.domain.api.models.ResponseModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Test response model.
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 */
@XmlRootElement(name = "pong")
public class PingResponseModel extends ResponseModel {

    private static final long serialVersionUID = -2838873002841632068L;

    @XmlAttribute(name = "data")
    public String data;
}
