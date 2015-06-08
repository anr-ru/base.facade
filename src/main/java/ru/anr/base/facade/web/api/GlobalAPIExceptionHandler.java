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
package ru.anr.base.facade.web.api;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.services.api.APICommandFactory;

/**
 * Global error handler for exceptions. It translate exception to a valid API
 * response.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 17, 2014
 *
 */
@ControllerAdvice
public class GlobalAPIExceptionHandler {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalAPIExceptionHandler.class);

    /**
     * API Factory reference
     */
    @Autowired
    private APICommandFactory apis;

    /**
     * A special case when the {@link NotFoundException} is thrown
     * 
     * @param rq
     *            The http request
     * @param ex
     *            The exception
     * @return Response body
     */
    @ExceptionHandler(value = { NotFoundException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String processNotFound(HttpServletRequest rq, Exception ex) {

        logger.debug("API not found Exception: {}", rq.getContextPath(), ex);

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }

    /**
     * Processing a global exception
     * 
     * @param rq
     *            Original Http request
     * @param ex
     *            An exception
     * @return String response
     * @throws Exception
     *             If re-thrown
     */
    @ExceptionHandler(value = { Exception.class, RuntimeException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String process(HttpServletRequest rq, Exception ex) throws Exception {

        logger.debug("API exception: {}", rq.getContextPath(), ex);

        if (ex instanceof WebAPIException) {
            throw (RuntimeException) ((WebAPIException) ex).getCause();
        }

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }
}
