package com.jlang.llvm.variables;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Type {
	INTEGER_32("i32", ".3"),
	DOUBLE("double", ".1"),
	STRING("i8*", ".5");

	private final String llvmVariableNameLiteral;
	private final String llvmStringPointer;
	private static final Map<String, Type> mapOfAntlrNameToVariableType = Map.of(
		"intem",
		INTEGER_32,
		"rzeczywiste",
		DOUBLE,
		"@strps",
		STRING
	);

	public static Type map(String antlrTypeName) {
		return mapOfAntlrNameToVariableType.get(antlrTypeName);
	}
}
