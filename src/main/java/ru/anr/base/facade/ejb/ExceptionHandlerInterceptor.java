/*
 * Copyright 2014-2023 the original author or authors.
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
package ru.anr.base.facade.ejb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import ru.anr.base.ApplicationException;
import ru.anr.base.domain.api.APIException;
import ru.anr.base.services.validation.ValidationUtils;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.transaction.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * The global exception handler for any EJB invocation. Allows to write
 * information about EJB exceptions in the application log, not only in the
 * server console as is done by default.
 *
 * @author Alexey Romanchuk
 * @created Jun 6, 2015
 */

public class ExceptionHandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerInterceptor.class);

    /**
     * The Intercepting Method
     *
     * @param context The invocation context
     * @return Result of the invocation
     * @throws Exception In case of error
     */
    @AroundInvoke
    public Object logExceptions(InvocationContext context) throws Exception {

        try {
            return context.proceed();
        } catch (Exception ex) {

            Throwable reason = new ApplicationException(ex).getMostSpecificCause();

            if (reason instanceof AuthenticationException) {
                logger.error("AuthenticationException: {}", reason.getMessage());
            } else if (reason instanceof AccessDeniedException) {
                logger.error("AccessDeniedException: {}", reason.getMessage());
            } else if (reason instanceof ConstraintViolationException) {
                Set<ConstraintViolation<?>> errors = ((ConstraintViolationException) reason).getConstraintViolations();
                logger.info("ConstraintViolationException: {}", ValidationUtils.getAllErrorsAsString(errors), reason);
            } else if (reason instanceof APIException) {
                APIException api = (APIException) reason;
                logger.info("API error: code={}/{}", api.getErrorCode(), api.getErrorId());
            } else if (reason instanceof RollbackException) {
                logger.error("ERROR: Rollback exception, maybe due to timeout: {}", reason.getMessage(), reason);
            } else if (reason instanceof ApplicationException) {
                logger.error("ERROR: ApplicationException: {}", reason.getMessage());
            } else {
                logger.error("Thrown an EJB exception: {}/{}", reason.getClass().getName(), reason.getMessage());
            }

            // Log details when
            if (logger.isDebugEnabled()) {
                logger.debug("Exception details: {}", reason.getMessage(), reason);
            }
            // Don't forget to re-throw!
            throw ex;
        }
    }
}
