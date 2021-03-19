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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.anr.base.ApplicationException;
import ru.anr.base.BaseParent;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.RawFormatTypes;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.services.api.APICommandFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilities for APICommand building in a web layer.
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 */

public final class CommandUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommandUtils.class);

    private CommandUtils() {
    }

    /**
     * Building a command with data extracted from {@link HttpServletRequest}
     * (method, headers).
     *
     * @param commandId  Identifier of Command
     * @param apiVersion API Version
     * @param request    Http request
     * @return A command instance
     */
    public static APICommand build(String commandId, String apiVersion, HttpServletRequest request) {

        APICommand cmd = new APICommand(commandId, apiVersion);

        if (MediaType.APPLICATION_XML_VALUE.equals(request.getContentType())) {
            cmd.setRequestFormat(RawFormatTypes.XML);
        } else {
            cmd.setRequestFormat(RawFormatTypes.JSON);
        }
        if (MediaType.APPLICATION_XML_VALUE.equals(request.getHeader("Accept"))) {
            cmd.setResponseFormat(RawFormatTypes.XML);
        } else {
            cmd.setResponseFormat(RawFormatTypes.JSON);
        }
        cmd.method(request.getMethod());

        cmd.getContexts().put("x-forwarded-for", request.getHeader("x-forwarded-for"));
        cmd.getContexts().put("user-agent", request.getHeader("user-agent"));

        return cmd;
    }

    /**
     * Exception enhancement - by default all our exception are packaged to
     * {@link APICommand} format but in some cases we need to throw an exception
     * (Spring security, Fatal error).
     *
     * @param api Original API command
     * @param ex  OPriginal exception
     * @return A new exception to be thrown or null if no throwing is required
     */
    public static RuntimeException enhanceException(APICommand api, Exception ex) {

        RuntimeException rs = null;

        ApplicationException exception = new ApplicationException(ex);
        Throwable root = exception.getRootCause();

        if ((root instanceof AccessDeniedException) || (root instanceof AuthenticationException)) {
            rs = new WebAPIException(root.getMessage(), root);
        } else {

            ResponseModel r = api.getResponse();
            if (BaseParent.safeEquals(r.getCode(), 1)) {

                // code = 1 means "System error" and must generate HTTP 500
                logger.error("System error caught: {}", r.getDescription());
                rs = new WebAPIException(r.getMessage(), root);
            }
        }
        return rs;
    }

    /**
     * Construction of API Command with request specific params (taken from a
     * current request)
     *
     * @param commandId  Identifier of command
     * @param apiVersion Version of API
     * @return An instance of API command
     */
    public static APICommand buildAPI(String commandId, String apiVersion) {

        ServletRequestAttributes r = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return CommandUtils.build(commandId, apiVersion, r.getRequest());
    }

    /**
     * Processing an API command. If an exception occurs, starting predefined
     * error processing.
     *
     * @param factory The {@link APICommandFactory}
     * @param api     API Command to process
     * @return Resulted command
     */
    public static APICommand process(APICommandFactory factory, APICommand api) {

        APICommand r;
        try {
            r = factory.process(api);

        } catch (Throwable ex) {

            Throwable e = new ApplicationException(ex).getMostSpecificCause();
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new ApplicationException(e); // Real shit happened
            }
        }
        return r;
    }
}
