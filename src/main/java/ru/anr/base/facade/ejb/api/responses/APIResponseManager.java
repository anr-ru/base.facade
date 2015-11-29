/**
 * 
 */
package ru.anr.base.facade.ejb.api.responses;

import ru.anr.base.domain.api.APICommand;

/**
 * This service encapsulates sending a message back to the sender both in the
 * case of error and a normal response.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2015
 *
 */

public interface APIResponseManager {

    /**
     * Sends a response to the response queue
     * 
     * @param request
     *            The original request
     * @param response
     *            The API result
     */
    void respond(APICommand request, APICommand response);

    /**
     * Sends a response in case of error
     * 
     * @param request
     *            The request
     * @param ex
     *            The exception to be processed
     */
    void error(APICommand request, Exception ex);
}
