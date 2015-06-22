/**
 * 
 */
package ru.anr.base.facade.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.anr.base.ApplicationException;

/**
 * The not-found exception. Generated when an object is not found. Normally in
 * the REST world causes the http 404 error.
 *
 *
 * @author Alexey Romanchuk
 * @created Jun 8, 2015
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends ApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 5402460084896920425L;

    /**
     * Default constructor
     * 
     * @param errorId
     *            Unique identifier of the error
     * @param msg
     *            The error message
     */
    public NotFoundException(String errorId, String msg) {

        super(msg);
        setErrorId(errorId);
    }
}
