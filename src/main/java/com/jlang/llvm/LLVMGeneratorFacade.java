package com.jlang.llvm;

import com.jlang.llvm.io.IOGenerator;
import com.jlang.llvm.io.IOIntermediaryCodeGenerator;
import com.jlang.llvm.variables.DeclarationGenerator;
import com.jlang.llvm.variables.IntermediaryDeclarationGenerator;
import com.jlang.llvm.variables.VariableType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class LLVMGeneratorFacade implements CodeGenerator {

	private IOGenerator ioGenerator;
	private DeclarationGenerator declarationGenerator;
	private int registry;

	public LLVMGeneratorFacade() {
		this.registry = 1;
		this.ioGenerator = new IOIntermediaryCodeGenerator();
		this.declarationGenerator = new IntermediaryDeclarationGenerator();
	}

	public String printf(String id, VariableType variableType) {
		var code = ioGenerator.printf(id, variableType, registry);
		registry += 2;
		return code;
	}

	public String scanf(String id, VariableType variableType) {
		var code = ioGenerator.scanf(id, variableType, registry);
		registry++;
		return code;
	}

	public String declare(String id, VariableType type) {
		return declarationGenerator.declare(id, type);
	}

	public String assign(String id, String value) {
		return declarationGenerator.assign(id, value);
	}

	@Override
	public int getRegistry() {
		return registry;
	}
}
