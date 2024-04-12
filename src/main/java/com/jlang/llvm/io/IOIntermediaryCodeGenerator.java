package com.jlang.llvm.io;

import com.jlang.llvm.variables.VariableType;

public class IOIntermediaryCodeGenerator implements IOGenerator {

	private static final String REGISTRY_SYMBOL = "%";
	private int registry;

	public IOIntermediaryCodeGenerator(int registry) {
		this.registry = registry;
	}

	@Override
	public int getRegistry() {
		return registry;
	}

	@Override
	public String printf(String id, VariableType variableType) {
		return (
			"%s = load %s, ptr %s\n".formatted(
					REGISTRY_SYMBOL + registry++,
					variableType.getLlvmVariableNameLiteral(),
					REGISTRY_SYMBOL + id
				) +
			"%s = call i32 (ptr, ...) @printf(ptr noundef @.str%s, %s %s)".formatted(
					REGISTRY_SYMBOL + registry++,
					variableType.getLlvmStringPointer(),
					variableType.getLlvmVariableNameLiteral(),
					REGISTRY_SYMBOL + (registry - 2)
				)
		);
	}

	@Override
	public String scanf(String id, VariableType variableType) {
		return """
                %s = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef @.str%s, ptr noundef %s)
                """.formatted(
				REGISTRY_SYMBOL + registry++,
				variableType.getLlvmStringPointer(),
				REGISTRY_SYMBOL + id
			);
	}
}
