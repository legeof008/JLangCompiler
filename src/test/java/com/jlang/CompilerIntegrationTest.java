package com.jlang;

import com.jlang.error.AssertingErrorLoggingBackend;
import org.antlr.v4.runtime.CharStreams;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

class CompilerIntegrationTest {

    private Compiler compiler;

    @Nested
    class Valid {

        private final AssertingErrorLoggingBackend assertingErrorLoggingBackend = new AssertingErrorLoggingBackend();

        @BeforeEach
        void setUp() {
            compiler = Compiler.withLogging(
                    assertingErrorLoggingBackend
            );
        }

        private static Stream<Arguments> testCompileValid() {
            return Stream.of(
                    of("no to mamy x co jest intem", "variable declaration"),
                    of("no to mamy x co jest intem\n", "variable declaration with newline"),
                    of("no to mamy x co jest  intem", "variable declaration with space"),
                    of("no to mamy  x  co jest  intem\n\t", "variable declaration with multiple space")
            );
        }

        @ParameterizedTest
        @MethodSource
        void testCompileValid(String rawInput, String description) {
            var input = CharStreams.fromString(rawInput);
            var output = compiler.compile(input);

            VavrAssertions.assertThat(output)
                    .as(description)
                    .isRight();
        }
    }

    @Nested
    class Invalid {

        @BeforeEach
        void setUp() {
            compiler = Compiler.withDefaults();
        }

        private static Stream<Arguments> testCompileWithError() {
            return Stream.of(
                    of("nno to mamy x co jest intem")
            );
        }

        @ParameterizedTest
        @MethodSource
        @Disabled("#13")
        void testCompileWithError(String rawInput) {
            compiler = Compiler.withDefaults(); // Do not fail if error encountered
            var input = CharStreams.fromString(rawInput);
            var output = compiler.compile(input);

            VavrAssertions.assertThat(output)
                    .isLeft();
        }
    }
}