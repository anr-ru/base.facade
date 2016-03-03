/**
 * 
 */
package ru.anr.base.facade.ejb.api.responses;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import ru.anr.base.domain.api.APICommand;
import ru.anr.base.facade.ejb.api.AsyncAPIHeaders;
import ru.anr.base.facade.ejb.mdb.BaseEventKeyStrategy;
import ru.anr.base.services.api.APICommandFactory;
import ru.anr.base.services.pattern.StrategyConfig;

/**
 * The base asynchronous API handler
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */
public class AsyncAPIStrategy extends BaseEventKeyStrategy {

    /**
     * The logger
     */
    private static Logger logger = LoggerFactory.getLogger(AsyncAPIStrategy.class);

    /**
     * The default constructor
     * 
     * @param keyName
     *            The name of the key
     */
    public AsyncAPIStrategy(String keyName) {

        setKeyValue(keyName);
    }

    /**
     * {@link APICommandFactory}
     */
    @Autowired
    private APICommandFactory apis;

    /**
     * The name of the strategy ID header
     */
    private static final String STRATEGY = AsyncAPIHeaders.API_STRATEGY_ID.name();

    /**
     * The name of the version header
     */
    private static final String VERSION = AsyncAPIHeaders.API_VERSION.name();

    /**
     * The name of the method header
     */
    private static final String METHOD = AsyncAPIHeaders.API_METHOD.name();

    /**
     * {@link APIResponseManager}
     */
    @Autowired
    private APIResponseManager responses;

    /**
     * {@inheritDoc}
     */
    @Override
    public Message<String> process(Message<String> m, StrategyConfig cfg) {

        logger.info("Processing API command: {}", m);

        APICommand cmd = new APICommand(header(m, STRATEGY, String.class), header(m, VERSION, String.class));

        cmd.setContexts(m.getHeaders());
        if (!isEmpty(m.getPayload())) {
            cmd.setRawModel(m.getPayload());
        }
        cmd.method(header(m, METHOD, String.class).toUpperCase(Locale.getDefault()));

        try {

            APICommand cmdx = apis.process(cmd);
            responses.respond(cmd, cmdx);

        } catch (Exception ex) {
            responses.error(cmd, ex);
            throw ex; // re-throw to complete the transaction roll-back
        }
        return null;
    }
}
