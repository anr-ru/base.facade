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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.glassfish.embeddable.*;
import org.glassfish.embeddable.archive.ScatteredArchive;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import ru.anr.base.ApplicationException;
import ru.anr.base.BaseParent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loader for Embedded Glassfish. Default configuration tries to load both
 * classpath and test classpath as a single war application.
 * <p>
 * The loader can be use only for JUnit tests and not specially usefull for
 * local web development, because unable to perform class/resources reloading.
 *
 * @author Alexey Romanchuk
 * @created Nov 17, 2014
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
     * Name of application
     */
    private final String appName;

    /**
     * Constructor
     *
     * @param appName Name of application
     */
    public GlassfishLoader(String appName) {

        super();
        this.appName = appName;
    }

    /**
     * Note: This part of code is taken from Apache Ant CommandLine
     * implementation.
     * <p>
     * Crack a command line.
     *
     * @param toProcess the command line to process.
     * @return the command line broken into strings. An empty or null toProcess
     * parameter results in a zero sized array.
     */
    public static String[] translateCommandline(String toProcess) {

        if (toProcess == null || toProcess.length() == 0) {
            // no command? no string
            return new String[0];
        }
        // parse with a simple finite state machine

        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = normal;
        final StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
        final ArrayList<String> result = new ArrayList<>();
        final StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;

        while (tok.hasMoreTokens()) {
            String nextTok = tok.nextToken();
            switch (state) {
                case inQuote:
                    if ("'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                case inDoubleQuote:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                default:
                    if ("'".equals(nextTok)) {
                        state = inQuote;
                    } else if ("\"".equals(nextTok)) {
                        state = inDoubleQuote;
                    } else if (" ".equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() != 0) {
                            result.add(current.toString());
                            current.setLength(0);
                        }
                    } else {
                        current.append(nextTok);
                    }
                    lastTokenHasBeenQuoted = false;
                    break;
            }
        }
        if (lastTokenHasBeenQuoted || current.length() != 0) {
            result.add(current.toString());
        }
        if (state == inQuote || state == inDoubleQuote) {
            throw new ApplicationException("unbalanced quotes in " + toProcess);
        }
        return result.toArray(new String[0]);
    }

    /**
     * Initialization and starting of Embedded Glassfish instance
     */
    public void initialize() {

        try {

            GlassFishProperties gfProps = new GlassFishProperties();

            gfProps.setConfigFileReadOnly(false);
            gfProps.setProperty("glassfish.embedded.tmpdir", "./target/glassfish");

            // Base logger settings
            Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);

            Logger.getLogger("javax.enterprise.system.tools.deployment").setLevel(Level.INFO);
            Logger.getLogger("javax.enterprise.system").setLevel(Level.FINE);

            glassfishRuntime = GlassFishRuntime.bootstrap();

            glassfish = glassfishRuntime.newGlassFish(gfProps);
            glassfish.start();

            /*
             * Executing command-line scripts
             */
            executeScript("META-INF/glassfish.properties.txt");
            executeScript("META-INF/glassfish.txt");

            glassfish.stop();
            glassfish.start();

        } catch (IOException | GlassFishException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Executes a file with asadmin commands
     *
     * @param fileName Name of a file
     * @throws IOException        In case of File not found
     * @throws GlassFishException In case of Glassfish error
     */
    private void executeScript(String fileName) throws IOException, GlassFishException {

        ClassPathResource script = new ClassPathResource(fileName);

        if (script.exists()) {

            List<String> lines = IOUtils.readLines(script.getInputStream(), BaseParent.DEFAULT_CHARSET);
            CommandRunner cmd = glassfish.getCommandRunner();

            for (String l : lines) {

                String[] tokens = translateCommandline(l);

                if (tokens.length == 0 || "#".equals(tokens[0])) {
                    continue;
                }

                String[] args = ArrayUtils.subarray(tokens, 1, tokens.length);

                logger.info("Runnning a command: {} {}", tokens[0], args);
                CommandResult r = cmd.run(tokens[0], args);

                switch (r.getExitStatus()) {
                    case SUCCESS:
                        logger.info("SUCCESS >> {}", r.getOutput());
                        break;
                    case WARNING:
                        logger.info("WARNING >> {} ( {} )", r.getOutput(), r.getFailureCause());
                        break;
                    case FAILURE:
                        logger.info("ERROR >> \n{}", r.getOutput(), r.getFailureCause());
                        throw new ApplicationException(r.getFailureCause());
                    default:
                }
            }
        } else {
            logger.info("'glassfish.txt' file not found");
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
     */
    public void deployApps() {

        try {

            Deployer deployer = glassfish.getDeployer();

            // Create a scattered web application.
            ScatteredArchive webmodule = new ScatteredArchive(appName, ScatteredArchive.Type.WAR, new File("src/main/webapp"));
            //new ScatteredArchive(appName, ScatteredArchive.Type.WAR, new File("src/main/webapp"));

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
     * @param webmodule WAR module
     * @param file      File to add
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
}
