package com.jlang;

import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompilerIntegrationTest {

    private Compiler compiler;

    @BeforeEach
    void setUp() {
        compiler = new Compiler();
    }

    @Test
    void testCompile() {
        var output = compiler.compile(CharStreams.fromString("no to mamy x co jest intem"));

        assertThat(output).isNotNull();
    }
}