/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.facade.ejb.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.anr.base.ApplicationException;

import javax.servlet.http.HttpServletRequest;

/**
 * A web controller for health management. It also processes a
 * {@link ServiceUnavailableException} exception as a 503 http error.
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 */
@RestController
@ControllerAdvice
public class HealthCheckController {

    /**
     * The logger
     */
    private final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

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
     * @param rq The http request
     * @param ex The exception
     * @return Response body
     */
    @ExceptionHandler(value = {ServiceUnavailableException.class})
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
     * @param fail true, if there is a need to throw a forced exception for tests
     * @return A json string containing the current status and a detailed
     * message if required
     */
    @RequestMapping(value = "/healthcheck/{fail}", method = RequestMethod.GET,
            produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String health(@PathVariable("fail") String fail) {

        return doCheck(fail);
    }

    /**
     * A handler for '/healthcheck' requests.
     *
     * @return A json string containing the current status and a detailed
     * message if required
     */
    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET,
            produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String health() {

        return doCheck(null);
    }

    /**
     * Delegates execution of checking to some underlying {@link HealthCheck}
     * service.
     *
     * @param fail "true", if it's necessary to throw an exception forcedly.
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
