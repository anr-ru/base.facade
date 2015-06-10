/**
 * 
 */
package ru.anr.base.facade.ejb;

import java.util.Set;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import ru.anr.base.ApplicationException;
import ru.anr.base.domain.api.APIException;
import ru.anr.base.services.ValidationUtils;

/**
 * Global Exception Handler for any EJB invocations. Allows to write in
 * appropriate log information about EJB exception, not only in the server
 * console.
 *
 *
 * @author Alexey Romanchuk
 * @created Jun 6, 2015
 *
 */

public class ExceptionHandlerInterceptor {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerInterceptor.class);

    /**
     * Intercepter Method
     * 
     * @param context
     *            The invocation context
     * @return Result of the invocation
     * 
     * @throws Exception
     *             In case of error
     */
    @AroundInvoke
    public Object logExceptions(InvocationContext context) throws Exception {

        try {
            return context.proceed();
        } catch (Exception ex) {

            Throwable reason = new ApplicationException(ex).getMostSpecificCause();

            if (reason instanceof AuthenticationException) {

                logger.error("Authentication error: {}", reason.getMessage());

            } else if (reason instanceof AccessDeniedException) {

                logger.error("Access denied: {}", reason.getMessage());

            } else if (reason instanceof ConstraintViolationException) {

                Set<ConstraintViolation<?>> errors = ((ConstraintViolationException) reason).getConstraintViolations();
                logger.info("Constraint Violation: {}", ValidationUtils.getAllErrorsAsString(errors));

            } else if (reason instanceof APIException) {

                APIException api = (APIException) reason;
                logger.info("API error: code={}, msg={}", api.getErrorCode(), api.getMessage());

            } else {
                logger.error("Thrown an EJB exception: {}", reason.getMessage(), ex);
            }

            // Don't forget to re-throw!
            throw ex;
        }
    }
}