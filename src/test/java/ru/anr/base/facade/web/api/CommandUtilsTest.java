package ru.anr.base.facade.web.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.MethodTypes;
import ru.anr.base.domain.api.RawFormatTypes;
import ru.anr.base.facade.samples.services.PingApiStrategy;
import ru.anr.base.tests.BaseTestCase;

/**
 * Tests for {@link CommandUtils}.
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
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

        Assertions.assertEquals(MethodTypes.Post, cmd.getType());

        Assertions.assertEquals(RawFormatTypes.XML, cmd.getRequestFormat());
        Assertions.assertEquals(RawFormatTypes.JSON, cmd.getResponseFormat());
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

        Assertions.assertEquals(MethodTypes.Get, cmd.getType());

        Assertions.assertEquals(RawFormatTypes.JSON, cmd.getRequestFormat());
        Assertions.assertEquals(RawFormatTypes.JSON, cmd.getResponseFormat());
    }

    /**
     * Use case: we can use the strategy class instead of string codes.
     */
    @Test
    public void testStrategyClass() {

        MockHttpServletRequest rq = new MockHttpServletRequest();
        rq.setMethod("GET");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(rq));

        APICommand cmd = CommandUtils.buildAPI(PingApiStrategy.class);
        Assertions.assertEquals("ping", cmd.getCommandId());
        Assertions.assertEquals("v1", cmd.getVersion());
    }
}

