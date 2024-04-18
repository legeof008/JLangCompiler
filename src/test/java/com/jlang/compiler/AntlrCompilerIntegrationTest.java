package com.jlang.compiler;

import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.jlang.error.AssertingErrorContext;
import java.io.ByteArrayInputStream;
import java.util.stream.Stream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

//TODO#21 - Add tests.
class AntlrCompilerIntegrationTest {

	private AntlrCompiler antlrCompiler;

	@Nested
	class Valid {

		private final AssertingErrorContext assertingErrorLoggingBackend = new AssertingErrorContext();

		@BeforeEach
		void setUp() {
			antlrCompiler = AntlrCompiler.withLogging(assertingErrorLoggingBackend);
		}

		private static Stream<Arguments> testCompileValid() {
			return Stream.of(
				of(
					"""
ciach ciach main () co jest intem tu jest start
no to mamy y rowne 1
no to mamy c rowne 1 + y
no to mamy m rowne 'c wychodzi:'
nazachodziejest(m)
nazachodziejest(c)
pach pach c no i tyle
""",
					"variable declaration"
				)
			);
		}

		@ParameterizedTest
		@MethodSource
		void testCompileValid(String rawInput, String description) {
			// given
			var input = CharStreams.fromString(rawInput);

			// when
			var output = antlrCompiler.compile(input);

			// then
			assertThat(output).as(description).isRight();
		}
	}

	@Disabled("TODO")
	@Nested
	class Invalid {

		@BeforeEach
		void setUp() {
			antlrCompiler = AntlrCompiler.withDefaults();
		}

		private static Stream<Arguments> testCompileWithError() {
			return Stream.of(of("nno to mamy x co jest intem"));
		}

		@ParameterizedTest
		@MethodSource
		void testCompileWithError(String rawInput) {
			// given
			var input = CharStreams.fromString(rawInput);

			// when
			var output = antlrCompiler.compile(input);

			// then
			assertThat(output).isLeft();
		}
	}
}
