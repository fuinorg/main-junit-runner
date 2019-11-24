/**
 * Copyright (C) 2019 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.mjunitrun;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.apache.commons.io.FileUtils;
import org.fuin.ext4logback.LogbackStandalone;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application that tests one or more JUnit tests.
 */
public final class JUnitApp {

    private static final Logger LOG = LoggerFactory.getLogger(JUnitApp.class);

    private static final String STATUS = "status";

    private static final String MESSAGE = "message";

    private static final String TRACE = "trace";

    private static final String DESCRIPTION = "description";

    private static String stackTraceAsString(final Exception ex) {
        final PrintWriter sw = new PrintWriter(new StringWriter());
        ex.printStackTrace(sw);
        return sw.toString();
    }

    private static void writeResult(final JsonObjectBuilder builder, final File resultDir, final String testName) {

        final String json = builder.build().toString();

        final File outFile = new File(resultDir, testName + ".json");
        try {
            FileUtils.write(outFile, json, "utf-8");
        } catch (final IOException ex) {
            LOG.error("Failed to write result to " + outFile);
            ex.printStackTrace(System.err);
        }

    }

    private static void initLogging(final String name, final File resultDir) {
        final File logFile = new File(resultDir, name + ".log");
        System.setProperty("log_file_path_and_name", logFile.getPath());
        final File logbackConfigSource = new File("logback.xml");
        final File logbackConfigTarget = new File(resultDir, "logback.xml");
        System.out.println("LOG FILE: " + logFile);
        if (logbackConfigTarget.exists()) {
            System.out.println("LOG CONFIG ALREADY EXISTS: " + logbackConfigTarget);
        } else {
            try {
                FileUtils.copyFile(logbackConfigSource, logbackConfigTarget);
            } catch (final IOException ex) {
                throw new RuntimeException("Failed to copy logback config from " + logbackConfigSource + " to " + logbackConfigTarget);
            }
            System.out.println("LOG CONFIG CREATED: " + logbackConfigTarget);
        }
        new LogbackStandalone().init(logbackConfigTarget);
    }

    /**
     * Runs the tests <b>without</b> initializing the logger and System.exit(int).
     * 
     * @param config Configuration to use.
     * 
     * @return Exit code.
     */
    public int execute(final JUnitAppConfig config) {

        // Prepare test
        final String appName = "Application " + config.getTestName();
        LOG.info(appName + " started");

        // Execute test
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        try {
            final Computer computer = new Computer();
            final JUnitCore jUnitCore = new JUnitCore();
            final Result result = jUnitCore.run(computer, config.getTestClass());
            builder.add(MESSAGE, "Executed: " + result.getRunCount() + ", Passed: " + (result.getRunCount() - result.getFailureCount())
                    + ", Failed: " + result.getFailureCount());
            if (result.getFailureCount() == 0) {
                builder.add(STATUS, "success");
                writeResult(builder, config.getDir(), config.getTestName());
                LOG.info(appName + " finished with: SUCCESS");
                return 0;
            } else {
                builder.add(STATUS, "failure");
                final JsonArrayBuilder arr = Json.createArrayBuilder();
                for (final Failure failure : result.getFailures()) {
                    if (failure.getException() != null) {
                        final JsonObjectBuilder failObj = Json.createObjectBuilder();
                        failObj.add(DESCRIPTION, failure.getTestHeader());
                        failObj.add(MESSAGE, failure.getMessage());
                        failObj.add(TRACE, failure.getTrace());
                        arr.add(failObj);
                        LOG.warn(failure.getTestHeader(), failure.getException());
                    }
                }
                builder.add("failures", arr);
                writeResult(builder, config.getDir(), config.getTestName());
                LOG.warn(appName + " finished with: FAILURE");
                return 0;
            }
        } catch (final RuntimeException ex) {
            builder.add(STATUS, "error");
            builder.add(MESSAGE, ex.getMessage());
            builder.add(TRACE, stackTraceAsString(ex));
            writeResult(builder, config.getDir(), config.getTestName());
            LOG.error(appName + " finished with: ERROR");
            return 1;
        }

    }

    /**
     * Runs the tests.
     * 
     * @param args
     *            See {@link JUnitAppConfig} for available arguments.
     */
    public static void main(final String[] args) {

        final JUnitAppConfig config = new JUnitAppConfig();
        final CmdLineParser parser = new CmdLineParser(config);
        try {
            parser.parseArgument(args);
            
            initLogging(config.getTestName(), config.getDir());

            System.exit(new JUnitApp().execute(config));

        } catch (final CmdLineException ex) {
            System.err.println(ex.getMessage());
            parser.printUsage(System.err);
            System.exit(2);
        }

    }

}
