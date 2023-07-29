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
package ru.anr.base.facade.ejb.health;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A exception for the 503 error.
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 */
@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends NestedRuntimeException {

    private static final long serialVersionUID = 5402460084896920425L;

    /**
     * A status value
     */
    private final String status;

    /**
     * Default constructor
     *
     * @param status Some status value (like UP, DOWN)
     * @param msg    A message with some details if applicable
     */
    public ServiceUnavailableException(String status, String msg) {
        super(msg);
        this.status = status;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
}
