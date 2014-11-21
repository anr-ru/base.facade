/**
 * 
 */
package ru.anr.base.facade.web.api;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.MethodTypes;
import ru.anr.base.domain.api.RawFormatTypes;
import ru.anr.base.tests.BaseTestCase;

/**
 * Tests for {@link CommandUtils}.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */
@ContextConfiguration(classes = CommandUtilsTest.class)
public class CommandUtilsTest extends BaseTestCase {

    /**
     * Use case: all context-type and accept are provided
     */
    @Test
    public void testBuild() {

        MockHttpServletRequest rq = new MockHttpServletRequest();
        rq.setMethod("POST");

        rq.setContentType(MediaType.APPLICATION_XML_VALUE);
        rq.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);

        APICommand cmd = CommandUtils.build("xxx", "v1", rq);

        Assert.assertEquals(MethodTypes.Post, cmd.getType());

        Assert.assertEquals(RawFormatTypes.XML, cmd.getRequestFormat());
        Assert.assertEquals(RawFormatTypes.JSON, cmd.getResponseFormat());
    }

    /**
     * Use case: accept is not provided, content-type is unsupported. The
     * default JSON format is applied in both cases.
     */
    @Test
    public void testBuildWrongHeaders() {

        MockHttpServletRequest rq = new MockHttpServletRequest();
        rq.setMethod("GET");

        rq.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        APICommand cmd = CommandUtils.build("xxx", "v1", rq);

        Assert.assertEquals(MethodTypes.Get, cmd.getType());

        Assert.assertEquals(RawFormatTypes.JSON, cmd.getRequestFormat());
        Assert.assertEquals(RawFormatTypes.JSON, cmd.getResponseFormat());
    }
}
