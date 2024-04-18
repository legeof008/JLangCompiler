package com.jlang;

import com.jlang.compiler.Compiler;
import com.jlang.compiler.CompilerFactory;
import com.jlang.llvm.llc.LLCCompilationStatus;
import com.jlang.llvm.llc.LLCProcess;
import io.vavr.control.Either;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.antlr.v4.runtime.CharStreams;

public class Main {

	// TODO#15 - better CLI handling
	public static void main(String[] args) throws IOException, InterruptedException {
		String filePath = args[0];

		final var contents = CharStreams.fromFileName(filePath);
		final var compiler = CompilerFactory.createWithDefaults();
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
			throws IOException, InterruptedException {
		System.out.println("Compilation successful");
		final var compiledCode = result.get();
		final var compiledFilePath = filePath.replace(".j", ".ll");
		Files.writeString(Path.of(compiledFilePath), compiledCode.output());
		System.out.println("Compiled code saved to " + compiledFilePath);
		compileToObjectFile(compiledFilePath);
	}

	private static void compileToObjectFile(String llFilePath) throws IOException, InterruptedException {
		var llcCompiler = new LLCProcess();
		var compilationResult = llcCompiler.compileToObjectFile(llFilePath);
		if (compilationResult == LLCCompilationStatus.SUCCESS) {
			System.out.println("Object file has been generated.");
		} else {
			System.out.println("Object file could not be generated.");
		}
	}
}
