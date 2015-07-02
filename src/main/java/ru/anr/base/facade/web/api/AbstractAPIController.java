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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ru.anr.base.ApplicationException;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.services.api.APICommandFactory;

/**
 * Base class for API Controllers. It encapsulates an API Factory, basic
 * settings for support both JSON/XML formats and also contains short-cut method
 * for fast building of API Command Object.
 *
 * All descendants must have
 * {@link org.springframework.web.bind.annotation.RestController} annotation and
 * method specific arguments, for example:
 * 
 * <PRE>
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * &#064;RequestMapping(value = &quot;/ping/{id}&quot;, method = RequestMethod.GET)
 * public String doGet(@PathVariable String id, @RequestParam Map&lt;String, String&gt; params) {
 * 
 *     APICommand cmd = buildAPI(&quot;ping&quot;, &quot;v1&quot;).context(&quot;id&quot;, id).params(params);
 *     return process(cmd).getRawModel();
 * }
 * </PRE>
 * 
 * or
 * 
 * <PRE>
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * &#064;RequestMapping(value = &quot;/ping/{id}&quot;, method = RequestMethod.POST)
 * public String doPost(@PathVariable String id, @RequestBody String body) {
 * 
 *     APICommand cmd = buildAPI(&quot;ping&quot;, &quot;v1&quot;).context(&quot;id&quot;, id).addRaw(body);
 *     return process(cmd).getRawModel();
 * }
 * </PRE>
 * 
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */
@RequestMapping(//
consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, //
produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Transactional(propagation = Propagation.NEVER)
public class AbstractAPIController {

    /**
     * Reference to API Command factory
     */
    @Autowired
    private APICommandFactory apis;

    /**
     * Construction of API Command with request specific params (taken from a
     * current request)
     * 
     * @param commandId
     *            Identifier of command
     * @param apiVersion
     *            Version of API
     * @return An instance of API command
     */
    protected APICommand buildAPI(String commandId, String apiVersion) {

        ServletRequestAttributes r = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return CommandUtils.build(commandId, apiVersion, r.getRequest());
    }

    /**
     * Processing an API command. If an exception occurs, starting predefined
     * error processing.
     * 
     * @param api
     *            API Command to process
     * @return Resulted command
     */
    protected APICommand process(APICommand api) {

        APICommand r = null;
        try {
            r = apis.process(api);

        } catch (Throwable ex) {

            Throwable e = new ApplicationException(ex).getMostSpecificCause();
            if (e != null && (e instanceof RuntimeException)) {
                throw (RuntimeException) e;
            } else {
                throw new ApplicationException(e); // Real shit happened
            }
        }
        return r;

    }
}
