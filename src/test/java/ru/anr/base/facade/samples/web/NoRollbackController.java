package ru.anr.base.facade.samples.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.anr.base.facade.samples.services.NoRollbackStrategy;
import ru.anr.base.facade.web.api.AbstractAPIController;

/**
 * Sample controller.
 *
 * @author Alexey Romanchuk
 * @created Mar 27, 2017
 */
@RequestMapping("/api/v1")
@RestController
public class NoRollbackController extends AbstractAPIController {

    /**
     * Sample query POST method.
     *
     * @param body Request body
     * @return Some result value
     */
    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    public String doPost(@RequestBody String body) {
        return process(buildAPI(NoRollbackStrategy.class).addRaw(body)).getRawModel();
    }

    /**
     * Sample query PUT method.
     *
     * @param body Request body
     * @return Some result value
     */
    @RequestMapping(value = "/norollback", method = RequestMethod.PUT)
    public String doPut(@RequestBody String body) {

        return process(buildAPI(NoRollbackStrategy.class)
                .addRaw(body)).getRawModel();
    }

}
