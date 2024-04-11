package com.jlang.llvm.variables;

import com.jlang.llvm.CodeGenerator;

public interface DeclarationGenerator extends CodeGenerator {
	String declare(String id, VariableType type);

	String assign(String id, String value);
}
