package com.jlang.error;

import com.jlang.dev.UnimplementedException;
import lombok.Builder;
import lombok.NonNull;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

@Builder
public class JErrorListener extends BaseErrorListener {

    @NonNull
    private ErrorLoggingBackend errorLoggingBackend;

    @Override
    public void syntaxError(@NonNull Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, @NonNull String msg, RecognitionException e) {
        var err = String.format("line %d:%d %s", line, charPositionInLine, msg);
        errorLoggingBackend.accept(err);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        throw new UnimplementedException(String.format("reportAmbiguity not implemented but called with startIndex=%d, stopIndex=%d, exact=%b, ambigAlts=%s",
                startIndex,
                stopIndex,
                exact,
                ambigAlts));
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        throw new UnimplementedException(String.format("reportAttemptingFullContext not implemented but called with startIndex=%d, stopIndex=%d, conflictingAlts=%s",
                startIndex,
                stopIndex,
                conflictingAlts));
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        throw new UnimplementedException(String.format("reportContextSensitivity not implemented but called with startIndex=%d, stopIndex=%d, prediction=%d",
                startIndex,
                stopIndex,
                prediction));
    }
}
