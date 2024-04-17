package com.jlang.llvm.llc;

import java.io.IOException;

public interface LLCCompiler {
    LLCCompilationStatus compileToObjectFile(String llFilePath) throws IOException, InterruptedException;
}
