/**
 * 
 */
package ru.anr.base.facade.ejb.api;

/**
 * The structure which contains the names of the headers used in async api
 * messages.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 20, 2015
 *
 */

public enum AsyncAPIHeaders {

    /**
     * What destination to use for the response
     */
    API_RESPONSE_TO,
    /**
     * The identifier of the API strategy
     */
    API_STRATEGY_ID,
    /**
     * The method of the API strategy (GET, POST, PUT, DELETE)
     */
    API_METHOD,
    /**
     * The version identifier of the strategy
     */
    API_VERSION,

    /**
     * The name of the prefix which separates API parameters
     */
    API_PARAM,

    /**
     * The identifier of a query
     */
    API_QUERY_ID
}
