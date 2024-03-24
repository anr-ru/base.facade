/*
 * Copyright 2014-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ru.anr.base.facade.ejb.api.responses;

import ru.anr.base.domain.api.APICommand;

/**
 * This service encapsulates sending a message back to the sender both in the
 * case of error and normal response.
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2015
 */
public interface APIResponseManager {
    /**
     * Sends a response to the response queue
     *
     * @param request  The original request
     * @param response The API result
     */
    void respond(APICommand request, APICommand response);

    /**
     * Sends a response in case of error
     *
     * @param request The request
     * @param ex      The exception to be processed
     */
    void error(APICommand request, Exception ex);
}
