/**
 * 
 */
package ru.anr.base.facade.web.api;

import org.springframework.http.ResponseEntity;

/**
 * The callback to wrap API calls.
 *
 * @author Alexey Romanchuk
 * @created May 23, 2016
 *
 */
@FunctionalInterface
public interface ApiCallback {

    /**
     * An API function call prototype
     * 
     * @param params
     *            Parameters to use
     * @return The response
     */
    ResponseEntity<String> doAPI(Object... params);
}
