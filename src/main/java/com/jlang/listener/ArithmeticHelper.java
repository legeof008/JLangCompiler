package com.jlang.listener;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ArithmeticHelper {
	//
	//	static boolean hasKnownValueVariableTokensinsideArithmeticExpression(
	//		final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues,
	//		final JlangParser.Arithmetic_assignmentContext ctx
	//	) {
	//		return declaredVariablesAndValues
	//			.keySet()
	//			.stream()
	//			.anyMatch(x ->
	//				ctx.arithmetic_statement().getText().contains(x) &&
	//				!Objects.equals(declaredVariablesAndValues.get(x).getKey(), "UNKNOWN")
	//			);
	//	}
	//
	//	static boolean hasValueVariableTokensinsideArithmeticExpression(
	//		final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues,
	//		final JlangParser.Arithmetic_assignmentContext ctx
	//	) {
	//		return declaredVariablesAndValues
	//			.keySet()
	//			.stream()
	//			.anyMatch(x -> ctx.arithmetic_statement().getText().contains(x));
	//	}
	//
	//	static Consumer<String> setVariablesInExpression(
	//		final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues,
	//		final Expression expression
	//	) {
	//		return variableToken ->
	//			expression.setVariable(
	//				variableToken,
	//				Double.parseDouble(declaredVariablesAndValues.get(variableToken).getKey())
	//			);
	//	}
}
