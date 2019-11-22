package org.fuin.mjunitrun;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Context;

public class TestJUnitApp {

    @Test
    public void testExecute() throws IOException {

        // PREPARE
        final String name = this.getClass().getSimpleName();
        final File jsonFile = new File("target/" + name + ".json");
        FileUtils.deleteQuietly(jsonFile);
        final List<ILoggingEvent> events = new ArrayList<>();
        final Logger root = (Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final AppenderBase<ILoggingEvent> mockAppender = new AppenderBase<ILoggingEvent>() {
            @Override
            protected void append(ILoggingEvent event) {
                events.add(event);
            }
        };
        mockAppender.setContext((Context) LoggerFactory.getILoggerFactory());
        mockAppender.setName("MOCK");
        mockAppender.start();
        root.addAppender(mockAppender);

        // TEST
        new JUnitApp().execute(new JUnitAppConfig(name, TestDummy.class, new File("target")));

        // VERIFY
        assertThat(events.size()).isEqualTo(2);
        assertThat(events.get(0).toString()).isEqualTo("[INFO] Application " + name + " started");
        assertThat(events.get(1).toString()).isEqualTo("[INFO] Application " + name + " finished with: SUCCESS");
        final String json = FileUtils.readFileToString(jsonFile, Charset.forName("utf-8"));
        assertThat(json).isEqualTo("{\"message\":\"Executed: 2, Passed: 2, Failed: 0\",\"status\":\"success\"}");
        
    }

}
