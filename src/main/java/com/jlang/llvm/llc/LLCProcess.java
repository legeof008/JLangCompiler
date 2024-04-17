package com.jlang.llvm.llc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LLCProcess implements LLCCompiler {
    @Override
    public LLCCompilationStatus compileToObjectFile(String llFilePath) throws IOException, InterruptedException {
        var process = new ProcessBuilder("llc", "--filetype=obj", llFilePath).start();
        while (process.isAlive()){
            TimeUnit.MILLISECONDS.sleep(10);
        }
        return process.exitValue() != 0 ? LLCCompilationStatus.FAILURE : LLCCompilationStatus.SUCCESS;
    }
}
