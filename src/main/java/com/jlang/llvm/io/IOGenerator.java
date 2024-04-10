package com.jlang.llvm.io;

import com.jlang.llvm.CodeGenerator;
import com.jlang.llvm.variables.VariableType;

public interface IOGenerator extends CodeGenerator {
    String printf(String id, VariableType variableType);

    String scanf(String id, VariableType variableType);

}
