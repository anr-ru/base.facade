/**
 *
 */
package ru.anr.base.facade.samples.web;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.anr.base.facade.samples.services.PingApiStrategy;
import ru.anr.base.facade.samples.services.PingErrorsStrategy;
import ru.anr.base.facade.web.api.AbstractAPIController;

import java.util.Map;

/**
 * Sample controller implementation - to check full glassfish application
 * integration stack.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */
@RequestMapping("/api/v1")
@Transactional
@RestController
public class ApiPingController extends AbstractAPIController {

    /**
     * Sample query GET method.
     *
     * @param id
     *            Some string
     * @param params
     *            Map with additional query params
     * @return Some result value
     */
    @RequestMapping(value = "/ping/{id}", method = RequestMethod.GET)
    public String doGet(@PathVariable String id, @RequestParam Map<String, String> params) {
        return process(buildAPI(PingApiStrategy.class)
                .context("id", id)
                .params(params)).getRawModel();
    }

    /**
     * Sample query POST method.
     *
     * @param id
     *            Some string
     * @param body
     *            Request body
     * @return Some result value
     */
    @RequestMapping(value = "/ping/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public String doPost(@PathVariable String id, @RequestBody String body) {

        return process(buildAPI(PingApiStrategy.class)
                .context("id", id)
                .addRaw(body)).getRawModel();
    }

    /**
     * Sample query DELETE method.
     *
     * @return Some result value
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String doDelete() {
        return process(buildAPI(PingApiStrategy.class)).getRawModel();
    }

    // ///////////////// Special tests for error

    /**
     * 403
     *
     * @return Some result value
     */
    @RequestMapping(value = "/denied", method = RequestMethod.POST)
    public String do403() {
        return process(buildAPI(PingErrorsStrategy.class)).getRawModel();
    }

    /**
     * 401
     *
     * @return Some result value
     */
    @RequestMapping(value = "/unauth", method = RequestMethod.GET)
    public String do401() {
        return process(buildAPI(PingErrorsStrategy.class)).getRawModel();
    }

    /**
     * 400 APIException
     *
     * @return Some result value
     */
    @RequestMapping(value = "/api", method = RequestMethod.DELETE)
    public String do400() {
        return process(buildAPI(PingErrorsStrategy.class)).getRawModel();
    }

    /**
     * 400 Validation
     *
     * @return Some result value
     */
    @RequestMapping(value = "/validate", method = RequestMethod.PUT)
    public String do400Validate() {
        return process(buildAPI(PingErrorsStrategy.class)).getRawModel();
    }

    /**
     * 500 System error
     *
     * @return Some result value
     */
    @RequestMapping(value = "/system", method = RequestMethod.PUT)
    public String do500() {
        return process(buildAPI(PingApiStrategy.class)).getRawModel();
    }
}
