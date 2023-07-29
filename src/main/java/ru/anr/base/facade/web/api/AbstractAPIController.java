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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.services.api.APICommandFactory;
import ru.anr.base.services.api.ApiCommandStrategy;

/**
 * The base class for API Controllers. It encapsulates an API Factory, basic
 * settings for support both JSON/XML formats and also contains short-cut method
 * for fast building of API Command Object.
 * <p>
 * All descendants must have a
 * {@link org.springframework.web.bind.annotation.RestController} annotation and
 * method specific arguments, for example:
 *
 * <PRE>
 * &#064;RequestMapping(value = &quot;/ping/{id}&quot;, method = RequestMethod.GET)
 * public String doGet(@PathVariable String id, @RequestParam Map&lt;String, String&gt; params) {
 *
 * APICommand cmd = buildAPI(PingStrategy.class).context(&quot;id&quot;, id).params(params);
 * return process(cmd).getRawModel();
 * }
 * </PRE>
 *
 * or
 *
 * <PRE>
 *
 * &#064;RequestMapping(value = &quot;/ping/{id}&quot;, method = RequestMethod.POST)
 * public String doPost(@PathVariable String id, @RequestBody String body) {
 *
 * APICommand cmd = buildAPI(PingStrategy.class).context(&quot;id&quot;, id).addRaw(body);
 * return process(cmd).getRawModel();
 * }
 * </PRE>
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 */
@RequestMapping(
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
        })
public class AbstractAPIController {

    /**
     * Reference to API Command factory
     */
    @Autowired
    private APICommandFactory apis;

    /**
     * Construction of API Command with use of a concrete API strategy class.
     * It's more precise way to specify the API command to use.
     *
     * @param clazz The class of the API strategy on the EJB side
     * @return A new instance of API command
     */
    protected APICommand buildAPI(Class<? extends ApiCommandStrategy> clazz) {
        return CommandUtils.buildAPI(clazz);
    }

    /**
     * Processing an API command. If an exception occurs, starting predefined
     * error processing.
     *
     * @param api API Command to process
     * @return Resulted command
     */
    protected APICommand process(APICommand api) {
        return CommandUtils.process(apis, api);
    }

    /**
     * Processing an error.
     *
     * @param api   API Command to process
     * @param ex    An exception
     * @param model A model
     * @return Resulted command
     */
    protected APICommand error(APICommand api, Exception ex, ResponseModel model) {
        return apis.error(api, ex, model);
    }
}
