package com.jlang.compiler;

import com.jlang.antlr.JlangLexer;
import com.jlang.antlr.JlangParser;
import com.jlang.error.ConsoleErrorContext;
import com.jlang.error.ErrorContext;
import com.jlang.error.JErrorListener;
import com.jlang.listener.JLangGeneratorListener;
import com.jlang.llvm.LLVMGeneratorFacade;
import io.vavr.control.Either;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Compiler {

	@NonNull
	private final ErrorContext errorContext;

	public static Compiler withDefaults() {
		return new Compiler(new ConsoleErrorContext());
	}

	public static Compiler withLogging(@NonNull ErrorContext errorContext) {
		return new Compiler(errorContext);
	}

	public Either<Failure, Output> compile(@NonNull CharStream input) {
		final var parser = getParserFor(input);
		parser.removeErrorListeners();
		parser.addErrorListener(JErrorListener.builder().errorContext(errorContext).build());

		final var ast = parser.program();
		final var walker = new ParseTreeWalker();

		final var codeGenerationFacade = new LLVMGeneratorFacade();
		final var listener = new JLangGeneratorListener(codeGenerationFacade);

		walker.walk(listener, ast);

		final var errors = errorContext.getErrors();
		if (!errors.isEmpty()) {
			return Either.left(new Failure(errors));
		}

		// TODO#21 - Gather compilation errors from listener
		final var compilationErrors = listener.getErrorsList();
		if (!compilationErrors.isEmpty()) {
			return Either.left(
				new Failure(
					compilationErrors
						.stream()
						.map(err -> String.format("Error at line %d: %s", err.line(), err.message()))
						.toList()
				)
			);
		}

		return Either.right(new Output(listener.getLLVMOutput()));
	}

	private static JlangParser getParserFor(CharStream input) {
		final var lexer = new JlangLexer(input);
		final var tokens = new CommonTokenStream(lexer);
		return new JlangParser(tokens);
	}

	public record Output(@NonNull String output) {}

	public record Failure(@NonNull List<String> messages) {}
}
