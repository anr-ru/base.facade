/**
 * 
 */
package ru.anr.base.facade.samples.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.services.api.APICommandFactory;

/**
 * Sample controller implementation - to check full glassfish application
 * integration stack.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */
@Controller
@RequestMapping("/api/v1")
@Transactional
public class ApiPingController {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ApiPingController.class);

    /**
     * A test EJB reference
     */
    @Autowired
    @Qualifier("apibean")
    private APICommandFactory apis;

    /**
     * Sample query GET method.
     * 
     * @param id
     *            Some string
     * @param params
     *            Map with additional query params
     * @return Some result value
     */
    @RequestMapping(value = "/ping/{id}", method = RequestMethod.GET, //
    produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public String doGet(@PathVariable String id, @RequestParam Map<String, String> params) {

        APICommand rs = apis.process(new APICommand("ping", "v1").context("id", id).method("GET")).params(params);
        logger.debug("Raw response: {}", rs.getRawModel());

        return rs.getRawModel();
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
    @RequestMapping(value = "/ping/{id}", method = RequestMethod.POST, //
    produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public String doPost(@PathVariable String id, @RequestBody String body) {

        APICommand cmd = new APICommand("ping", "v1").context("id", id).method("POST").addRaw(body);
        APICommand rs = apis.process(cmd);

        logger.debug("Raw response: {}", rs.getRawModel());
        return rs.getRawModel();
    }
}
