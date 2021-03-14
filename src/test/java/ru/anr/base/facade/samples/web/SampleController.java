/**
 *
 */
package ru.anr.base.facade.samples.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.anr.base.facade.samples.ejb.MyService;
import ru.anr.base.facade.web.api.NotFoundException;
import ru.anr.base.samples.domain.Samples;

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
@Controller
@RequestMapping("/api/v1")
@Transactional
public class SampleController {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

    /**
     * A test EJB reference
     */
    @Autowired
    @Qualifier("ejbean")
    private MyService service;

    /**
     * Sample query GET method.
     *
     * @param id
     *            Some string
     * @return Some result value
     */
    @RequestMapping(value = "/query/{id}", method = RequestMethod.GET, produces = {"text/plain"})
    @ResponseBody
    public String displayMessage(@PathVariable String id) {

        Samples s = service.doThat(id);

        logger.debug("Created an entity: {}", s);
        return s.getId().toString();
    }

    /**
     * Sample query GET method.
     *
     * @param params
     *            Some get uri variables
     * @return Some result value
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET, //
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String get(@RequestParam Map<String, String> params) {

        logger.debug("GET: {}", params);

        String v = params.getOrDefault("q", "");
        return "{\"value\": \"" + v + "\"}";
    }

    /**
     * Sample query POST method.
     *
     * @param body
     *            Request body
     * @return Some result value
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String post(@RequestBody String body) {

        logger.debug("POST: {}", body);
        return body;
    }

    /**
     * Sample query PUT method.
     *
     * @return Some result value
     */
    @RequestMapping(value = "/put", method = RequestMethod.PUT, //
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> put() {

        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    /**
     * Sample query PUT method.
     *
     * @param what
     *            Some path parameter
     *
     * @return Some result value
     */
    @RequestMapping(value = "/delete/{what}", method = RequestMethod.DELETE, //
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> delete(@PathVariable String what) {

        return new ResponseEntity<>("", HttpStatus.ALREADY_REPORTED);
    }

    /**
     * A sample method which raises the NotFoundException
     *
     * @param what
     *            Some path parameter
     *
     * @return Some result value
     */
    @RequestMapping(value = "/notfound/{what}", method = RequestMethod.PUT, //
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> notFound(@PathVariable String what) {

        throw new NotFoundException("what", what);
    }
}
