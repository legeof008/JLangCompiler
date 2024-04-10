package com.jlang;

import com.jlang.error.AssertingErrorLoggingBackend;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.vavr.api.VavrAssertions.assertThat;
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
                    of("no to mamy  x  co jest  intem\n\t", "variable declaration with multiple space"),
                    of("x bedzie drodzy panstwo 2", "variable assignment"),
                    of("x bedzie drodzy panstwo 2 + 1", "variable arithmetic assignment")
            );
        }

        @ParameterizedTest
        @MethodSource
        void testCompileValid(String rawInput, String description) {
            // given
            var input = CharStreams.fromString(rawInput);

            // when
            var output = compiler.compile(input);

            // then
            assertThat(output)
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
            // given
            var input = CharStreams.fromString(rawInput);

            // when
            var output = compiler.compile(input);

            // then
            assertThat(output)
                    .isLeft();
        }
    }
}