package com.jlang.listener;

import com.jlang.antlr.JlangParser;
import com.jlang.llvm.variables.VariableType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.objecthunter.exp4j.Expression;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ArithmeticHelper {
    static boolean hasVariableTokensinsideArithmeticExpression(final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues, final JlangParser.Arithmetic_assignmentContext ctx) {
        return declaredVariablesAndValues.keySet().stream().anyMatch(getExpressionSubstring(ctx));
    }

    static Predicate<String> getExpressionSubstring(JlangParser.Arithmetic_assignmentContext ctx) {
        return variableToken -> ctx.getText().substring(ctx.ID().getText().length()).contains(variableToken);
    }

    static Consumer<String> setVariablesInExpression(final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues, final Expression expression) {
        return variableToken -> expression.setVariable(variableToken, Double.parseDouble(declaredVariablesAndValues.get(variableToken).getKey()));
    }

}
