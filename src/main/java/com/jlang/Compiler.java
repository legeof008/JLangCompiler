package com.jlang;

import com.jlang.antlr.JlangBaseListener;
import com.jlang.antlr.JlangLexer;
import com.jlang.antlr.JlangParser;
import com.jlang.error.ConsoleErrorLoggingBackend;
import com.jlang.error.ErrorLoggingBackend;
import com.jlang.error.JErrorListener;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Compiler {

    @NonNull
    private final ErrorLoggingBackend errorLoggingBackend;

    public static Compiler withDefaults() {
        return new Compiler(
                new ConsoleErrorLoggingBackend()
        );
    }

    public static Compiler withLogging(@NonNull ErrorLoggingBackend errorLoggingBackend) {
        return new Compiler(errorLoggingBackend);
    }

    // TODO: Errors?
    public Either<Failure, Output> compile(@NonNull CharStream input) {
        final var parser = getParserFor(input);
        parser.removeErrorListeners();
        parser.addErrorListener(JErrorListener.builder().errorLoggingBackend(errorLoggingBackend).build());

        final var ast = parser.program();
        final var walker = new ParseTreeWalker();
        final var listener = new JlangBaseListener(); // TODO: Implement listener

        walker.walk(listener, ast);

        // TODO: Get LLVM IR from listener
        return Either.right(new Output("implement me!")); // TODO: Return LLVM IR or failure
    }

    private static JlangParser getParserFor(CharStream input) {
        final var lexer = new JlangLexer(input);
        final var tokens = new CommonTokenStream(lexer);
        return new JlangParser(tokens);
    }

    public record Output(String output) {
    }

    public record Failure(String message) {
    }
}
