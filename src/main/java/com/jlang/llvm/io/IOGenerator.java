package com.jlang.llvm.io;

import com.jlang.llvm.variables.VariableType;

public interface IOGenerator {
	String printf(String id, VariableType variableType, int registry);

	String scanf(String id, VariableType variableType, int registry);
}
