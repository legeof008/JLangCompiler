package com.jlang.listener;

import static com.jlang.listener.ArithmeticHelper.*;
import static com.jlang.listener.IOHelper.*;
import static com.jlang.listener.ProgramInitHelper.*;

import com.jlang.antlr.JlangBaseListener;
import com.jlang.antlr.JlangParser;
import com.jlang.error.CompilationLogicError;
import com.jlang.llvm.LLVMGeneratorFacade;
import com.jlang.llvm.variables.VariableType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class JLangGeneratorListener extends JlangBaseListener {

	private final LLVMGeneratorFacade codeGenerationFacade;
	private final List<String> programParts;
	private final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues;
	private final List<CompilationLogicError> errorsList;

	public JLangGeneratorListener(LLVMGeneratorFacade codeGenerationFacade) {
		this.codeGenerationFacade = codeGenerationFacade;
		this.programParts = new ArrayList<>();
		this.declaredVariablesAndValues = new HashMap<>();
		this.errorsList = new ArrayList<>();
	}

	@Override
	public void enterProgram(JlangParser.ProgramContext ctx) {
		programParts.add(PRINTF_AND_SCANF_UTILITY_STRINGS_DECLARATION);
		programParts.add(MAIN_FUNCTION_START);
	}

	@Override
	public void exitProgram(JlangParser.ProgramContext ctx) {
		programParts.add(MAIN_FUNCTION_END);
		programParts.add(PRINTF_SCANF_DECLERATIONS);
	}

	@Override
	public void enterVariable_declaration(JlangParser.Variable_declarationContext ctx) {
		var variableTypeAntlrName = ctx.NUMBER_TYPE().getText();

		declaredVariablesAndValues.put(
				ctx.ID().getText(),
				Map.entry("UNKNOWN", VariableType.map(variableTypeAntlrName))
		);

		programParts.add(
				codeGenerationFacade.declare(ctx.ID().getText(), VariableType.map(variableTypeAntlrName))
		);
	}

	@Override
	public void enterArithmetic_assignment(JlangParser.Arithmetic_assignmentContext ctx) {
		if (hasKnownValueVariableTokensinsideArithmeticExpression(declaredVariablesAndValues, ctx)) {

			var expression = new ExpressionBuilder(ctx.arithmetic_statement().getText())
					.variables(declaredVariablesAndValues.keySet())
					.build();
			declaredVariablesAndValues
					.keySet()
					.forEach(setVariablesInExpression(declaredVariablesAndValues, expression));
			addLineDependingOnDataType(ctx, expression);
		} else if (hasValueVariableTokensinsideArithmeticExpression(declaredVariablesAndValues, ctx)) {


		} else {
			var expression = new ExpressionBuilder(ctx.arithmetic_statement().getText()).build();
			addLineDependingOnDataType(ctx, expression);
		}
	}

	@Override
	public void enterFunction_call(JlangParser.Function_callContext ctx) {
		if (
				allVariablesInArgumentsPreviouslyDeclared(declaredVariablesAndValues, ctx) &&
						onlyOneVariableHandlingRestrictionMet(ctx)
		) {
			handleSingleArgumentOperations(ctx);
		}
	}

	private void handleSingleArgumentOperations(JlangParser.Function_callContext ctx) {
		if (isStdPrintfOperation(ctx)) {
			programParts.add(
					codeGenerationFacade.printf(
							ctx.argument_list().getText(),
							declaredVariablesAndValues.get(ctx.argument_list().getText()).getValue()
					)
			);
		}
		if (isStdScanfOperation(ctx)) {
			programParts.add(
					codeGenerationFacade.scanf(
							ctx.argument_list().getText(),
							declaredVariablesAndValues.get(ctx.argument_list().getText()).getValue()
					)
			);
		}
	}

	private void addLineDependingOnDataType(
			JlangParser.Arithmetic_assignmentContext ctx,
			Expression expression
	) {
		switch (declaredVariablesAndValues.get(ctx.ID().getText()).getValue()) {
			case Double -> programParts.add(
					codeGenerationFacade.assign(ctx.ID().getText(), "%f".formatted(expression.evaluate()))
			);
			case Integer32 -> programParts.add(
					codeGenerationFacade.assign(
							ctx.ID().getText(),
							"%d".formatted(Math.round(expression.evaluate()))
					)
			);
		}
	}

	public String getLLVMOutput() {
		return String.join("\n", programParts);
	}
}
