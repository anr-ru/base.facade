/**
 * 
 */
package ru.anr.base.facade.web.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.services.api.APICommandFactory;

/**
 * Global error handler for exceptions. It translate exception to a valid API
 * response (like {@link ru.anr.base.domain.api.ErrorModel}).
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 17, 2014
 *
 */
@ControllerAdvice
public class GlobalAPIExceptionHandler {

    /**
     * API Factory reference
     */
    @Autowired
    private APICommandFactory apis;

    /**
     * Processing a global exception
     * 
     * @param rq
     *            Original Http request
     * @param ex
     *            An exception
     * @return String response
     * @throws Exception
     *             If rethrown
     */
    @ExceptionHandler(value = { Exception.class, RuntimeException.class })
    @ResponseBody
    public String process(HttpServletRequest rq, Exception ex) throws Exception {

        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            throw ex; // Pre-defined exception handler exists
        }

        APICommand cmd = apis.error(ex);
        return cmd.getRawModel();
    }
}
