/**
 * 
 */
package ru.anr.base.facade.ejb.health;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.anr.base.ApplicationException;

/**
 * A web controller for health management. It also processes a
 * {@link ServiceUnavailableException} exception as a 503 http error.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 *
 */
@RestController
@ControllerAdvice
public class HealthCheckController {

    /**
     * The logger
     */
    private Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    /**
     * A template for a response without a message
     */
    private static final String JSON_TEMPLATE = "{\"status\" : \"%s\"}";

    /**
     * A template for a response with a message
     */
    private static final String JSON_MSG_TEMPLATE = "{\"status\" : \"%s\", \"message\":\"%s\"}";

    /**
     * A special case when the {@link ServiceUnavailableException} is thrown
     * 
     * @param rq
     *            The http request
     * @param ex
     *            The exception
     * @return Response body
     */
    @ExceptionHandler(value = { ServiceUnavailableException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public String processServiceUnavailable(HttpServletRequest rq, Exception ex) {

        if (logger.isDebugEnabled()) {
            logger.debug("Caught an exception for : " + rq.getContextPath(), ex);
        } else {
            logger.info("Service unavailable: {}", ex.getMessage());
        }
        return String.format(JSON_TEMPLATE, "FAILURE");
    }

    /**
     * A reference to a health-check service
     */
    @Autowired(required = false)
    private HealthCheck healthCheck;

    /**
     * A handler for '/healthcheck' requests.
     * 
     * @param fail
     *            true, if there is a need to throw a forced exception for tests
     * @return A json string containing the current status and a detailed
     *         message if required
     */
    @RequestMapping(value = "/healthcheck/{fail}", method = RequestMethod.GET,
            produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String health(@PathVariable("fail") String fail) {

        return doCheck(fail);
    }

    /**
     * A handler for '/healthcheck' requests.
     * 
     * @return A json string containing the current status and a detailed
     *         message if required
     */
    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET,
            produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String health() {

        return doCheck(null);
    }

    /**
     * Delegates execution of checking to some underlying {@link HealthCheck}
     * service.
     * 
     * @param fail
     *            "true", if it's necessary to throw an exception forcedly.
     * @return Some response text
     */
    private String doCheck(String fail) {

        Boolean b = (fail == null) ? false : Boolean.parseBoolean(fail);
        try {
            /*
             * if there is no health-check service defined, we suppose not to
             * use a health-check and return always Http.OK
             */
            return (healthCheck == null) ? String.format(JSON_TEMPLATE, "NONE")
                    : String.format(JSON_MSG_TEMPLATE, "UP", healthCheck.check(b));
        } catch (RuntimeException ex) {
            throw (RuntimeException) new ApplicationException(ex).getMostSpecificCause();
        }
    }
}
