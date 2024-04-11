package com.jlang.llvm;

import com.jlang.llvm.io.IOGenerator;
import com.jlang.llvm.io.IOIntermediaryCodeGenerator;
import com.jlang.llvm.variables.DeclarationGenerator;
import com.jlang.llvm.variables.IntermediaryDeclarationGenerator;
import com.jlang.llvm.variables.VariableType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class LLVMGeneratorFacade implements IOGenerator, DeclarationGenerator {

	private IOGenerator ioGenerator;
	private DeclarationGenerator declarationGenerator;
	private int registry;

	public LLVMGeneratorFacade() {
		this.registry = 1;
		this.ioGenerator = new IOIntermediaryCodeGenerator(registry);
		this.declarationGenerator = new IntermediaryDeclarationGenerator(registry);
	}

	@Override
	public int getRegistry() {
		return registry;
	}

	@Override
	public String printf(String id, VariableType variableType) {
		return ioGenerator.printf(id, variableType);
	}

	@Override
	public String scanf(String id, VariableType variableType) {
		return ioGenerator.scanf(id, variableType);
	}

	@Override
	public String declare(String id, VariableType type) {
		return declarationGenerator.declare(id, type);
	}

	@Override
	public String assign(String id, String value) {
		return declarationGenerator.assign(id, value);
	}
}
