package com.jlang;

import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.jlang.error.AssertingErrorContext;
import java.util.stream.Stream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CompilerIntegrationTest {

	private Compiler compiler;

	@Nested
	class Valid {

		private final AssertingErrorContext assertingErrorLoggingBackend = new AssertingErrorContext();

		@BeforeEach
		void setUp() {
			compiler = Compiler.withLogging(assertingErrorLoggingBackend);
		}

		private static Stream<Arguments> testCompileValid() {
			return Stream.of(
				of(
					"""
no to mamy x co jest intem
no to mamy y co jest rzeczywiste
no to mamy z rowne 2
z bedzie drodzy panstwo 3
y bedzie drodzy panstwo 2.5 + 0.1 - (1.0 * 5.0)
no to mamy u rowne 2.5 * y
nazachodziejest(u)
nazachodziejest(2+2)
lewarekazapraweucho(&z)
z bedzie drodzy panstwo z + 1
nazachodziejest(z)
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
			var output = compiler.compile(input);

			// then
			assertThat(output).as(description).isRight();
		}
	}

	@Disabled("TODO")
	@Nested
	class Invalid {

		@BeforeEach
		void setUp() {
			compiler = Compiler.withDefaults();
		}

		private static Stream<Arguments> testCompileWithError() {
			return Stream.of(of("nno to mamy x co jest intem"));
		}

		@ParameterizedTest
		@MethodSource
		//        @Disabled("#13")
		void testCompileWithError(String rawInput) {
			// given
			var input = CharStreams.fromString(rawInput);

			// when
			var output = compiler.compile(input);

			// then
			assertThat(output).isLeft();
		}
	}
}
