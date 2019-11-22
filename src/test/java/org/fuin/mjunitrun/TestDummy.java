package org.fuin.mjunitrun;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TestDummy {

    @Test
    public void testHello1() {
        assertThat("hello1").isEqualTo("hello1");
    }

    @Test
    public void testTrue() {
        assertThat(true).isEqualTo(true);
    }
    
}
