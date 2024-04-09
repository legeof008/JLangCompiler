package com.jlang;

import com.jlang.error.ErrorLoggingBackend;
import org.antlr.v4.runtime.CharStreams;
import org.assertj.core.api.Assertions;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

class CompilerIntegrationTest {

    private final AssertingErrorLoggingBackend assertingErrorLoggingBackend = new AssertingErrorLoggingBackend();
    private Compiler compiler;

    @BeforeEach
    void setUp() {
        compiler = Compiler.withLogging(
                assertingErrorLoggingBackend
        );
    }

    private static Stream<Arguments> testCompileValid() {
        return Stream.of(
                of("no to mamy x co jest intem"),
                of("no to mamy x co jest intem\n"),
                of("no  to  mamy  x  co  jest  intem\n") // TODO: Fix: Unexpected error reported by analysis: line 1:27 no viable alternative at input 'jestintem'
        );
    }

    @ParameterizedTest
    @MethodSource
    void testCompileValid(String rawInput) {
        var input = CharStreams.fromString(rawInput);
        var output = compiler.compile(input);

        VavrAssertions.assertThat(output)
                .isRight();
    }

    private static Stream<Arguments> testCompileWithError() {
        return Stream.of(
                of("nno to mamy x co jest intem")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testCompileWithError(String rawInput) {
        compiler = Compiler.withDefaults(); // Do not fail if error encountered
        var input = CharStreams.fromString(rawInput);
        var output = compiler.compile(input);

        VavrAssertions.assertThat(output)
                .isLeft();
    }

    private static class AssertingErrorLoggingBackend implements ErrorLoggingBackend {

        @Override
        public void accept(String message) {
            Assertions.fail("Unexpected error reported by analysis: " + message);
        }
    }
}