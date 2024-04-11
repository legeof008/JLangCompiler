package com.jlang.llvm.variables;

import lombok.NonNull;

public class IntermediaryDeclarationGenerator implements DeclarationGenerator {

	private static final String REGISTRY_SYMBOL = "%";

	private int registry;

	public IntermediaryDeclarationGenerator(int registry) {
		this.registry = registry;
	}

	@Override
	public int getRegistry() {
		return registry;
	}

	@Override
	public String declare(@NonNull String id, @NonNull VariableType type) {
		return "%s = alloca %s%n".formatted(REGISTRY_SYMBOL + id, type.getLlvmVariableNameLiteral());
	}

	@Override
	public String assign(@NonNull String id, @NonNull String value, @NonNull VariableType type) {
		return "store i32 %s, ptr %s".formatted(value, REGISTRY_SYMBOL + id);
	}
}
