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
package ru.anr.base.facade.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.anr.base.ApplicationException;

/**
 * The Not-Found exception. It is generated when an object is not found.
 * Normally in the REST world causes the http 404 error.
 *
 * @author Alexey Romanchuk
 * @created Jun 8, 2015
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends ApplicationException {

    private static final long serialVersionUID = 5402460084896920425L;

    /**
     * Default constructor
     *
     * @param errorId Unique identifier of the error
     * @param msg     The error message
     */
    public NotFoundException(String errorId, String msg) {
        super(msg);
        setErrorId(errorId);
    }
}
