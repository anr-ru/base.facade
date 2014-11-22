/**
 * 
 */
package ru.anr.base.facade.tests;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import ru.anr.base.ApplicationException;

/**
 * Loader for Embedded Glassfish. Default configuration tries to load both
 * classpath and test classpath as a single war application.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 17, 2014
 *
 */

public class GlassfishLoader {

    /**
     * Logger
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GlassfishLoader.class);

    /**
     * Static ref to Glassfish instance
     */
    protected GlassFish glassfish;

    /**
     * File with domain configuration in class path
     */
    private String domainFileConfig = "domain.xml";

    /**
     * Name of application
     */
    private final String appName;

    /**
     * Constructor
     * 
     * @param appName
     *            Name of application
     */
    public GlassfishLoader(String appName) {

        super();
        this.appName = appName;
    }

    /**
     * Initialization and starting of Embedded Glassfish instance
     */
    public void initialize() {

        try {

            GlassFishProperties gfProps = new GlassFishProperties();

            // Searching of domain.xml in classpath
            gfProps.setConfigFileURI(new ClassPathResource(domainFileConfig).getFile().toURI().toString());
            gfProps.setConfigFileReadOnly(true);

            // Base logger settings
            Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);

            Logger.getLogger("javax.enterprise.system.tools.deployment").setLevel(Level.FINEST);
            Logger.getLogger("javax.enterprise.system").setLevel(Level.INFO);

            glassfish = GlassFishRuntime.bootstrap().newGlassFish(gfProps);
            glassfish.start();

        } catch (IOException | GlassFishException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Deploying all applications (EJB, Servlets) localed both in classpath and
     * test classpath.
     * 
     * @param deployer
     * @throws IOException
     * @throws GlassFishException
     */
    public void deployApps() {

        try {

            Deployer deployer = glassfish.getDeployer();

            // Create a scattered web application.
            ScatteredArchive webmodule = new ScatteredArchive(appName, ScatteredArchive.Type.WAR);

            // target/classes directory contains my complied servlets
            webmodule.addClassPath(new File("target", "classes"));
            webmodule.addClassPath(new File("target", "test-classes"));

            // WEB application if required
            safeAddMetadata(webmodule, new File("src/main/webapp/WEB-INF", "sun-web.xml"));
            safeAddMetadata(webmodule, new File("src/main/webapp/WEB-INF", "web.xml"));

            // EJB application if required
            safeAddMetadata(webmodule, new File("target/classes/META-INF", "ejb-jar.xml"));
            safeAddMetadata(webmodule, new File("target/test-classes/META-INF", "ejb-jar.xml"));

            safeAddMetadata(webmodule, new File("target/classes/META-INF", "glassfish-ejb-jar.xml"));
            safeAddMetadata(webmodule, new File("target/test-classes/META-INF", "glassfish-ejb-jar.xml"));

            // JPA config if required
            safeAddMetadata(webmodule, new File("target/classes/META-INF", "persistence.xml"));
            safeAddMetadata(webmodule, new File("target/test-classes/META-INF", "persistence.xml"));

            deployer.deploy(webmodule.toURI());

        } catch (IOException | GlassFishException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Safe adding item to avoid {@link IOException} in case of absence some
     * configuration. For example a test application can be web only without
     * EJB.
     * 
     * @param webmodule
     *            WAR module
     * @param file
     *            File to add
     */
    private void safeAddMetadata(ScatteredArchive webmodule, File file) {

        try {

            logger.debug("Trying to load: {}", file);
            webmodule.addMetadata(file);
            logger.info("Loaded: {}", file);

        } catch (IOException ex) {
            logger.error("File : {} not found, ignoring", file);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param domainFileConfig
     *            the domainFileConfig to set
     */
    public void setDomainFileConfig(String domainFileConfig) {

        this.domainFileConfig = domainFileConfig;
    }

}
