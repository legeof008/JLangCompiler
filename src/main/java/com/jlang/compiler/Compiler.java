package com.jlang.compiler;

import io.vavr.control.Either;
import java.io.InputStream;
import java.util.List;
import lombok.NonNull;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

public interface Compiler {
	Either<Failure, Output> compile(@NonNull CharStream input);

	record Output(@NonNull String output) {}

	record Failure(@NonNull List<String> messages) {}
}
