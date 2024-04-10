package com.jlang.llvm.variables;

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
    public String declare(String id, VariableType type) {
        return "%s = alloca %s\n".formatted(REGISTRY_SYMBOL + id, type.getLlvmVariableNameLiteral());
    }

    @Override
    public String assign(String id, String value) {
        return "store i32 %s, ptr %s".formatted(value, REGISTRY_SYMBOL + id);
    }
}
