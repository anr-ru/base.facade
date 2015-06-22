/**
 * 
 */
package ru.anr.base.facade.web.api;

import ru.anr.base.ApplicationException;

/**
 * Used to show that an exception was thrown due to exception processing
 * process.
 *
 *
 * @author Alexey Romanchuk
 * @created Jun 3, 2015
 *
 */

public class WebAPIException extends ApplicationException {

    /**
     * Serial
     */
    private static final long serialVersionUID = 9160830457750893589L;

    /**
     * @param msg
     *            A message
     * @param cause
     *            The cause
     */
    public WebAPIException(String msg, Throwable cause) {

        super(msg, cause);
    }
}
