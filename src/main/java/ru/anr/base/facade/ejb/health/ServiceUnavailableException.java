/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A exception for the 503 error.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 *
 */
@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends NestedRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5402460084896920425L;

    /**
     * A status value
     */
    private String status;

    /**
     * Default constructor
     * 
     * @param status
     *            Some status value (like UP, DOWN)
     * @param msg
     *            A message with some details if applicable
     */
    public ServiceUnavailableException(String status, String msg) {

        super(msg);
        this.status = status;
    }

    /**
     * @return the status
     */
    public String getStatus() {

        return status;
    }
}
