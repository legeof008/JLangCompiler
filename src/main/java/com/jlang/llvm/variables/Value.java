package com.jlang.llvm.variables;

public record Value(String value, Type type) {
	@Deprecated
	public static Value atRegistry(int registry, Type type) {
		return new Value("%" + registry, type);
	}
}
