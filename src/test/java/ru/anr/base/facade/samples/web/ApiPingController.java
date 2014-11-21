/**
 * 
 */
package ru.anr.base.facade.samples.web;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.facade.web.api.AbstractAPIController;

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

        APICommand cmd = buildAPI("ping", "v1").context("id", id).params(params);
        return process(cmd).getRawModel();
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
    @RequestMapping(value = "/ping/{id}", method = RequestMethod.POST)
    public String doPost(@PathVariable String id, @RequestBody String body) {

        APICommand cmd = buildAPI("ping", "v1").context("id", id).addRaw(body);
        return process(cmd).getRawModel();
    }

    /**
     * Sample query DELETE method.
     * 
     * @return Some result value
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String doDelete() {

        APICommand cmd = buildAPI("ping", "v1");
        return process(cmd).getRawModel();
    }
}
