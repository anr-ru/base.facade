/*
 * Copyright 2014-2022 the original author or authors.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.APIException;
import ru.anr.base.services.api.APICommandFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * A global error handler for all exceptions. It translates an exception to a valid
 * API response.
 *
 * @author Alexey Romanchuk
 * @created Nov 17, 2014
 */
@ControllerAdvice
public class GlobalAPIExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalAPIExceptionHandler.class);

    /**
     * {@link APICommandFactory}
     */
    @Autowired
    private APICommandFactory apis;

    /**
     * A special case when the {@link HttpRequestMethodNotSupportedException} is
     * thrown.
     *
     * @param rq The http request
     * @param ex The exception
     * @return The response body
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public String processMethodUnsupported(HttpServletRequest rq, Exception ex) {

        logger.debug("Caught exception: " + rq.getContextPath(), ex);

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }

    /**
     * A special case when the {@link APIException} is thrown
     *
     * @param rq The http request
     * @param ex The exception
     * @return The response body
     */
    @ExceptionHandler(value = {APIException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String processAPIException(HttpServletRequest rq, Exception ex) {

        logger.debug("Caught exception: " + rq.getContextPath(), ex);

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }

    /**
     * A special case when the {@link ConstraintViolationException} is thrown
     *
     * @param rq The http request
     * @param ex The exception
     * @return The response body
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String processConstraintViolationException(HttpServletRequest rq, Exception ex) {

        logger.debug("Caught exception: " + rq.getContextPath(), ex);

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }

    /**
     * A special case when the {@link NotFoundException} is thrown
     *
     * @param rq The http request
     * @param ex The exception
     * @return The response body
     */
    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String processNotFound(HttpServletRequest rq, Exception ex) {

        logger.debug("Caught exception: " + rq.getContextPath(), ex);

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }

    /**
     * A special case when the {@link AccessDeniedException} is thrown
     *
     * @param rq The http request
     * @param ex The exception
     * @return The response body
     */
    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String processAccessDenied(HttpServletRequest rq, Exception ex) {

        logger.debug("Caught exception: " + rq.getContextPath(), ex);

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }

    /**
     * A special case when the {@link AuthenticationException} is thrown
     *
     * @param rq The http request
     * @param ex The exception
     * @return The response body
     */
    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String processAuthenticationException(HttpServletRequest rq, Exception ex) {

        logger.debug("Caught exception: " + rq.getContextPath(), ex);

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }

    /**
     * Processes general types of exceptions - generating a 500 Internal Server
     * Error
     *
     * @param rq Original Http request
     * @param ex An exception
     * @return The string response
     * @throws Exception If re-thrown
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class, Throwable.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String process(HttpServletRequest rq, Exception ex) throws Exception {

        logger.debug("Caught exception: " + rq.getContextPath(), ex);

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }
}
