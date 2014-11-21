/**
 * 
 */
package ru.anr.base.facade.web.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.domain.api.RawFormatTypes;

/**
 * Description ...
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */

public final class CommandUtils {

    /**
     * Constructor
     */
    private CommandUtils() {

        /*
         * Do nothing
         */
    }

    /**
     * Building a command with data extracted from {@link HttpServletRequest}
     * (method, headers).
     * 
     * 
     * @param commandId
     *            Identifier of Command
     * @param apiVersion
     *            API Version
     * @param request
     *            Http request
     * @return A command instance
     */
    public static APICommand build(String commandId, String apiVersion, HttpServletRequest request) {

        APICommand cmd = new APICommand(commandId, apiVersion);

        if (MediaType.APPLICATION_XML_VALUE.equals(request.getContentType())) {
            cmd.setRequestFormat(RawFormatTypes.XML);
        } else {
            cmd.setRequestFormat(RawFormatTypes.JSON);
        }
        if (MediaType.APPLICATION_XML_VALUE.equals(request.getHeader("Accept"))) {
            cmd.setResponseFormat(RawFormatTypes.XML);
        } else {
            cmd.setResponseFormat(RawFormatTypes.JSON);
        }
        cmd.method(request.getMethod());
        return cmd;
    }
}
