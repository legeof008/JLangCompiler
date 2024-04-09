package com.jlang;

import com.jlang.antlr.JlangBaseListener;
import com.jlang.antlr.JlangLexer;
import com.jlang.antlr.JlangParser;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@NoArgsConstructor
public class Compiler {

    // TODO: Errors?
    public Output compile(@NonNull CharStream input) {
        final var parser = getParserFor(input);

        final var ast = parser.program();
        final var walker = new ParseTreeWalker();
        final var listener = new JlangBaseListener(); // TODO: Implement listener

        walker.walk(listener, ast);

        // TODO: Get LLVM IR from listener
        return new Output("Mocked output!");
    }

    private static JlangParser getParserFor(CharStream input) {
        final var lexer = new JlangLexer(input);
        final var tokens = new CommonTokenStream(lexer);
        return new JlangParser(tokens);
    }

    public record Output(String output) {
    }
}
