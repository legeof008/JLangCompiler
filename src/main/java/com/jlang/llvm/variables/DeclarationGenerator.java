package com.jlang.llvm.variables;


public interface DeclarationGenerator {
	String declare(String id, VariableType type);

	String assign(String id, String value);
}
