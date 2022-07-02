package ru.anr.base.tests.facade;

import org.junit.jupiter.api.Disabled;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import ru.anr.base.tests.AbstractGlassfishWebTestCase;
import ru.anr.base.tests.JmsTests;

import javax.jms.Destination;

/**
 * Base test config
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 */
@ContextConfiguration(locations = "classpath:tests-context.xml")
@Disabled
public class BaseWebTestCase extends AbstractGlassfishWebTestCase {

    // Change the logger's output
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    /**
     * {@link JmsTests}
     */
    @Autowired
    protected JmsTests jms;

    /**
     * the test queue
     */
    @Autowired
    @Qualifier("queue")
    protected Destination queue;
}
