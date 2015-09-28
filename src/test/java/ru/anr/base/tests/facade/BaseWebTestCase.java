/**
 * 
 */
package ru.anr.base.tests.facade;

import javax.jms.Destination;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import ru.anr.base.tests.AbstractGlassfishWebTestCase;
import ru.anr.base.tests.JmsTests;

/**
 * Base test config
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */
@ContextConfiguration(locations = "classpath:tests-context.xml")
@Ignore
public class BaseWebTestCase extends AbstractGlassfishWebTestCase {

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
