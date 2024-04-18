package com.jlang.compiler;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class CompilerFactory {

	public static Compiler createWithDefaults() {
		return AntlrCompiler.withDefaults();
	}
}
