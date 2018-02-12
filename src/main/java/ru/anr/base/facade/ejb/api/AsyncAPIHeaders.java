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
package ru.anr.base.facade.ejb.api;

/**
 * A structure which contains names of the headers used in async api messages.
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
    API_QUERY_ID,

    /**
     * The header that is used to organize parallel processing
     */
    PARALLEL_HASH;
}
