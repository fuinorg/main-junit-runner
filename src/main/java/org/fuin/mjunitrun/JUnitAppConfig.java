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

    /**
     * Default constructor.
     */
    public JUnitAppConfig() {
        super();
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
     */
    public JUnitAppConfig(String testName, Class<?> testClass, File dir) {
        super();
        this.testName = testName;
        this.testClass = testClass.getName();
        this.dir = dir;
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

}
