package com.jlang.llvm.io;

import com.jlang.llvm.CodeGenerator;
import com.jlang.llvm.variables.Type;

public interface IOGenerator extends CodeGenerator {
	String printf(String id, Type type);

	String scanf(String id, Type type);
}
