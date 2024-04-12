package com.jlang.llvm.variables;

public record Value(String value, VariableType type) {
	public static Value atRegistry(int registry, VariableType type) {
		return new Value("%" + registry, type);
	}
}
