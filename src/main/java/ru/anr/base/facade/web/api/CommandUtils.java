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
package ru.anr.base.facade.web.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.RawFormatTypes;

/**
 * Utils for APICOmmand building in a web layer.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */

public final class CommandUtils {

    /**
     * Constructor
     */
    private CommandUtils() {

        /*
         * Do nothing
         */
    }

    /**
     * Building a command with data extracted from {@link HttpServletRequest}
     * (method, headers).
     * 
     * 
     * @param commandId
     *            Identifier of Command
     * @param apiVersion
     *            API Version
     * @param request
     *            Http request
     * @return A command instance
     */
    public static APICommand build(String commandId, String apiVersion, HttpServletRequest request) {

        APICommand cmd = new APICommand(commandId, apiVersion);

        if (MediaType.APPLICATION_XML_VALUE.equals(request.getContentType())) {
            cmd.setRequestFormat(RawFormatTypes.XML);
        } else {
            cmd.setRequestFormat(RawFormatTypes.JSON);
        }
        if (MediaType.APPLICATION_XML_VALUE.equals(request.getHeader("Accept"))) {
            cmd.setResponseFormat(RawFormatTypes.XML);
        } else {
            cmd.setResponseFormat(RawFormatTypes.JSON);
        }
        cmd.method(request.getMethod());
        return cmd;
    }
}
