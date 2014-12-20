/*
 * Copyright 2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.tests;

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
 * <p>
 * The loader can be use only for JUnit tests and not specially usefull for
 * local web development, because unable to perform class/resources reloading.
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
     * Static ref to GlassfishRuntime
     */
    protected GlassFishRuntime glassfishRuntime;

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
            gfProps.setProperty("glassfish.embedded.tmpdir", "./target/glassfish");

            // Base logger settings
            Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);

            Logger.getLogger("javax.enterprise.system.tools.deployment").setLevel(Level.FINEST);
            Logger.getLogger("javax.enterprise.system").setLevel(Level.INFO);

            glassfishRuntime = GlassFishRuntime.bootstrap();

            glassfish = glassfishRuntime.newGlassFish(gfProps);
            glassfish.start();

        } catch (IOException | GlassFishException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Shutdown instance
     */
    public void shutdown() {

        try {

            glassfish.dispose();
            glassfishRuntime.shutdown();

        } catch (GlassFishException ex) {
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
            ScatteredArchive webmodule =
                    new ScatteredArchive(appName, ScatteredArchive.Type.WAR, new File("src/main/webapp"));

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
            logger.info("File : {} not found, ignoring", file);
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