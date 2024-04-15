package com.jlang.compiler;

import io.vavr.control.Either;
import java.io.InputStream;
import java.util.List;
import lombok.NonNull;

public interface Compiler {
	Either<Failure, Output> compile(@NonNull InputStream input);

	record Output(@NonNull String output) {}

	record Failure(@NonNull List<String> messages) {}
}
