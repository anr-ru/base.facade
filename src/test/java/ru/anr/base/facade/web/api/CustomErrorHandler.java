package ru.anr.base.facade.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alexey Romanchuk
 * @created Sep 21, 2021
 */
@ControllerAdvice
public class CustomErrorHandler extends GlobalAPIExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorHandler.class);

    @Override
    protected void logException(HttpServletRequest rq, Throwable ex, HttpStatus status) {
        if (logger.isDebugEnabled()) {
            logger.debug("(overridden) Caught exception: (" + status.value() + "), " + rq.getContextPath(), ex);
        }
    }
}
