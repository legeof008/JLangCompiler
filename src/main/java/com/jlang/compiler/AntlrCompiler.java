package com.jlang.compiler;

import com.jlang.antlr.JlangLexer;
import com.jlang.antlr.JlangParser;
import com.jlang.error.ConsoleErrorContext;
import com.jlang.error.ErrorContext;
import com.jlang.error.JErrorListener;
import com.jlang.listener.JLangGeneratorListener;
import com.jlang.llvm.LLVMGeneratorFacade;
import io.vavr.control.Either;
import java.io.IOException;
import java.io.InputStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class AntlrCompiler implements Compiler {

	@NonNull
	private final ErrorContext errorContext;

	static AntlrCompiler withDefaults() {
		return new AntlrCompiler(new ConsoleErrorContext());
	}

	static AntlrCompiler withLogging(@NonNull ErrorContext errorContext) {
		return new AntlrCompiler(errorContext);
	}

	public Either<Failure, Output> compile(@NonNull InputStream input) {
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

	private static JlangParser getParserFor(InputStream input) {
		final JlangLexer lexer;
		try {
			lexer = new JlangLexer(CharStreams.fromStream(input));
		} catch (IOException e) {
			throw new RuntimeException(e); //TODO#22 - Handle exception
		}
		final var tokens = new CommonTokenStream(lexer);
		return new JlangParser(tokens);
	}
}
