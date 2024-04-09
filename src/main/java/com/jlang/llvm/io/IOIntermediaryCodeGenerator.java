package com.jlang.llvm.io;

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
    public String printf(String id) {
        return """
                %s = load i32, i32* %s
                %s = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strp, i32 0, i32 0), i32 %s
                """.formatted(REGISTRY_SYMBOL + registry++, REGISTRY_SYMBOL + id, REGISTRY_SYMBOL + registry++, REGISTRY_SYMBOL + (registry - 2));
    }

    @Override
    public String scanf(String id) {
        return """
                %s = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strs, i32 0, i32 0), i32* %s
                """.formatted(REGISTRY_SYMBOL + registry++, REGISTRY_SYMBOL + id);
    }
}
