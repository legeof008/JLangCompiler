package com.jlang.listener;

import com.jlang.antlr.JlangParser;
import com.jlang.llvm.variables.VariableType;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.objecthunter.exp4j.Expression;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ArithmeticHelper {

	static boolean hasKnownValueVariableTokensinsideArithmeticExpression(
			final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues,
			final JlangParser.Arithmetic_assignmentContext ctx
	) {
		return declaredVariablesAndValues
				.keySet()
				.stream()
				.anyMatch(x ->
						ctx.arithmetic_statement().getText().contains(x) &&
								!Objects.equals(declaredVariablesAndValues.get(x).getKey(), "UNKNOWN")
				);
	}

	static boolean hasValueVariableTokensinsideArithmeticExpression(
			final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues,
			final JlangParser.Arithmetic_assignmentContext ctx
	) {
		return declaredVariablesAndValues
				.keySet()
				.stream()
				.anyMatch(x ->
						ctx.arithmetic_statement().getText().contains(x)
				);
	}

	static Consumer<String> setVariablesInExpression(
			final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues,
			final Expression expression
	) {
		return variableToken ->
				expression.setVariable(
						variableToken,
						Double.parseDouble(declaredVariablesAndValues.get(variableToken).getKey())
				);
	}
}
