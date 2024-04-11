package com.jlang.llvm.variables;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum VariableType {
	Integer32("i32", ".3"),
	Double("double", ".1");

	private final String llvmVariableNameLiteral;
	private final String llvmStringPointer;
	private static final Map<String, VariableType> mapOfAntlrNameToVariableType = Map.of(
		"intem",
		Integer32,
		"rzeczywiste",
		Double
	);

	public static VariableType map(String antlrTypeName) {
		return mapOfAntlrNameToVariableType.get(antlrTypeName);
	}
}
