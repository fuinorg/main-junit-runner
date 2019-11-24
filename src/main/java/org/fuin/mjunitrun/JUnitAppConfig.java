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

import org.kohsuke.args4j.Option;

/**
 * Configuration for the JUnit runner application.
 */
public final class JUnitAppConfig {

    @Option(name = "-name", usage = "Sets the unique test name", metaVar = "TEST_NAME", required = true)
    private String testName;

    @Option(name = "-class", usage = "Sets the fully qualified name of the junit test class", metaVar = "TEST_CLASS", required = true)
    private String testClass;

    @Option(name = "-dir", usage = "Sets the directory to write the result to", metaVar = "RESULT_DIR", required = true)
    private File dir;

    @Option(name = "-logbackXmlSrc", usage = "Logback XML configuration file to copy if it does not exist in the target directory (default is 'logback.xml' in current directory))", metaVar = "LOGBACK_XML_SRC", required = false)
    private File logbackXmlSrc;

    /**
     * Default constructor.
     */
    public JUnitAppConfig() {
        super();
        logbackXmlSrc = new File("logback.xml");
    }
    
    /**
     * Constructor without logback config XML file.
     * 
     * @param testName
     *            Unique name of the test.
     * @param testClass
     *            Test class to execute.
     * @param dir
     *            Directory for results.
     * @param logbackXmlSrc
     *            Logback XML configuration file to copy if it does not exist in the target directory.
     */
    public JUnitAppConfig(final String testName, final Class<?> testClass, final File dir) {
        this(testName, testClass, dir, null);
    }
    
    /**
     * Constructor with all data.
     * 
     * @param testName
     *            Unique name of the test.
     * @param testClass
     *            Test class to execute.
     * @param dir
     *            Directory for results.
     * @param logbackXmlSrc
     *            Logback XML configuration file to copy if it does not exist in the target directory.
     */
    public JUnitAppConfig(final String testName, final Class<?> testClass, final File dir, final File logbackXmlSrc) {
        super();
        this.testName = testName;
        this.testClass = testClass.getName();
        this.dir = dir;
        if (logbackXmlSrc == null) {
            this.logbackXmlSrc = new File("logback.xml");
        } else {
            this.logbackXmlSrc = logbackXmlSrc;
        }
    }

    /**
     * Returns the unique test name.
     * 
     * @return Test name.
     */
    public final String getTestName() {
        return testName;
    }

    /**
     * Sets the test name to anew value.
     * 
     * @param testName
     *            Test name to set.
     */
    public final void setTestName(final String testName) {
        this.testName = testName;
    }

    /**
     * Returns the test test class.
     * 
     * @return Test class to execute.
     */
    public final Class<?> getTestClass() {
        try {
            return Class.forName(testClass);
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Sets the test test class to a new value.
     * 
     * @param testClass
     *            Test class.
     */
    public final void setTestClass(final Class<?> testClass) {
        this.testClass = testClass.getName();
    }

    /**
     * Returns the base directory.
     * 
     * @return Working directory for log configuration and results.
     */
    public final File getDir() {
        return dir;
    }

    /**
     * Sets the base directory to a new value.
     * 
     * @param dir
     *            Working directory for log configuration and results.
     */
    public final void setDir(final File dir) {
        this.dir = dir;
    }

    /**
     * Returns the Logback XML configuration file to copy if it does not exist in the target directory.
     * 
     * @return Location of config file.
     */
    public final File getLogbackXmlSrc() {
        return logbackXmlSrc;
    }

    /**
     * Sets the Logback XML configuration file to copy if it does not exist in the target directory.
     * 
     * @param logbackXmlSrc
     *            Location of config file to set.
     */
    public final void setLogbackXmlSrc(final File logbackXmlSrc) {
        this.logbackXmlSrc = logbackXmlSrc;
    }

}
