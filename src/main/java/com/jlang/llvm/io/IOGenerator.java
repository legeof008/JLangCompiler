package com.jlang.llvm.io;

import com.jlang.llvm.CodeGenerator;

public interface IOGenerator extends CodeGenerator {
    String printf(String id);

    String scanf(String id);

}
