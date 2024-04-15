package com.jlang;

import com.jlang.compiler.Compiler;
import io.vavr.control.Either;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.antlr.v4.runtime.CharStreams;

public class Main {

	// TODO#15 - better CLI handling
	public static void main(String[] args) throws IOException {
		String filePath = args[0];

		final var contents = CharStreams.fromFileName(filePath);
		final var compiler = Compiler.withDefaults();
		final var result = compiler.compile(contents);

		if (result.isRight()) {
			saveResult(result, filePath);
		} else {
			reportFailure(result);
		}
	}

	private static void reportFailure(Either<Compiler.Failure, Compiler.Output> result) {
		System.out.println("Compilation failed");
		System.out.println(result.getLeft().messages());
	}

	private static void saveResult(Either<Compiler.Failure, Compiler.Output> result, String filePath)
		throws IOException {
		System.out.println("Compilation successful");
		final var compiledCode = result.get();
		final var compiledFilePath = filePath.replace(".j", ".ll");
		Files.writeString(Path.of(compiledFilePath), compiledCode.output());
		System.out.println("Compiled code saved to " + compiledFilePath);
	}
}
