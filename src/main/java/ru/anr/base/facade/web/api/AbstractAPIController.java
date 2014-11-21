/**
 * 
 */
package ru.anr.base.facade.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.services.api.APICommandFactory;

/**
 * Base class for API Controllers. It incapsulates an API Factory, basic
 * settings for support both JSON/XML formats and also contains short-cut method
 * for fast building of API Command Object.
 *
 * All descendants must have
 * {@link org.springframework.web.bind.annotation.RestController.RestController}
 * annotation and method specific arguments, for example:
 * 
 * <PRE>
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
     * Processing an API command
     * 
     * @param api
     *            API Command to process
     * @return Resulted command
     */
    protected APICommand process(APICommand api) {

        return apis.process(api);
    }
}
