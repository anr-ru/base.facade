/**
 * 
 */
package ru.anr.base.facade.tests;

import java.io.IOException;

import org.glassfish.embeddable.GlassFishException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.slf4j.LoggerFactory;

import ru.anr.base.tests.BaseTestCase;

/**
 * Abstract configuration for test case which uses Embedded Glassfish to deploy
 * application for local testing.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */
@Ignore
public class AbstractGlassfishWebTestCase extends BaseTestCase {

    /**
     * Logger
     */
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractGlassfishWebTestCase.class);

    /**
     * Static ref to Glassfish instance
     */
    protected static GlassfishLoader loader;

    /**
     * Initialization and starting of Embedded Glassfish instance
     * 
     * @throws GlassFishException
     *             on some configuration exception
     * @throws IOException
     *             If some files not found
     */
    @BeforeClass
    public static synchronized void initialize() throws GlassFishException, IOException {

        if (loader == null) {

            synchronized (AbstractGlassfishWebTestCase.class) {

                loader = new GlassfishLoader("facade");

                loader.initialize();
                loader.deployApps();
            }
        }
    }
}
