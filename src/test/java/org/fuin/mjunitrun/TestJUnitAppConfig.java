package org.fuin.mjunitrun;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.fuin.utils4j.Utils4J;
import org.junit.Test;

public class TestJUnitAppConfig {

    @Test
    public void testCreateWithSetters() {

        final JUnitAppConfig testee = new JUnitAppConfig();
        testee.setTestName("Abc");
        testee.setDir(Utils4J.getTempDir());
        testee.setTestClass(TestDummy.class);

        assertThat(testee.getLogbackXmlSrc()).isEqualTo(new File("logback.xml"));
        testee.setLogbackXmlSrc(new File("/var/tmp/logback.xml"));

        assertThat(testee.getTestName()).isEqualTo("Abc");
        assertThat(testee.getDir()).isEqualTo(Utils4J.getTempDir());
        assertThat(testee.getTestClass()).isEqualTo(TestDummy.class);
        assertThat(testee.getLogbackXmlSrc()).isEqualTo(new File("/var/tmp/logback.xml"));

    }

    @Test
    public void testCreateWithAllConstructor() {

        final JUnitAppConfig testee = new JUnitAppConfig("Abc", TestDummy.class, Utils4J.getTempDir(), new File("/var/tmp/logback.xml"));

        assertThat(testee.getTestName()).isEqualTo("Abc");
        assertThat(testee.getDir()).isEqualTo(Utils4J.getTempDir());
        assertThat(testee.getTestClass()).isEqualTo(TestDummy.class);
        assertThat(testee.getLogbackXmlSrc()).isEqualTo(new File("/var/tmp/logback.xml"));

    }

    @Test
    public void testCreateWithMandatoryConstructor() {

        final JUnitAppConfig testee = new JUnitAppConfig("Abc", TestDummy.class, Utils4J.getTempDir());

        assertThat(testee.getTestName()).isEqualTo("Abc");
        assertThat(testee.getDir()).isEqualTo(Utils4J.getTempDir());
        assertThat(testee.getTestClass()).isEqualTo(TestDummy.class);

    }
    
}
