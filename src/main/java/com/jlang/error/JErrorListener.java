package com.jlang.error;

import lombok.Builder;
import lombok.NonNull;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

@Builder
public class JErrorListener extends BaseErrorListener {

    @NonNull
    private ErrorLoggingBackend errorLoggingBackend;

    @Override
    public void syntaxError(@NonNull Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, @NonNull String msg, RecognitionException e) {
        var err = String.format("line %d:%d %s", line, charPositionInLine, msg);
        errorLoggingBackend.accept(err);
    }
}
