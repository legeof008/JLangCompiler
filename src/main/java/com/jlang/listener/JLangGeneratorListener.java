package com.jlang.listener;

import static com.jlang.listener.ProgramInitHelper.MAIN_FUNCTION_END;
import static com.jlang.listener.ProgramInitHelper.MAIN_FUNCTION_START;
import static com.jlang.listener.ProgramInitHelper.PRINTF_AND_SCANF_UTILITY_STRINGS_DECLARATION;
import static com.jlang.listener.ProgramInitHelper.PRINTF_SCANF_DECLERATIONS;

import com.jlang.antlr.JlangBaseListener;
import com.jlang.antlr.JlangParser;
import com.jlang.error.CompilationLogicError;
import com.jlang.llvm.LLVMGeneratorFacade;
import com.jlang.llvm.variables.Value;
import com.jlang.llvm.variables.VariableType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

public class JLangGeneratorListener extends JlangBaseListener {

	private final LLVMGeneratorFacade codeGenerationFacade;
	private final List<String> programParts;
	private final Map<String, VariableType> variables;
	private final Deque<Value> stack;

	@Getter
	private final List<CompilationLogicError> errorsList;

	public JLangGeneratorListener(LLVMGeneratorFacade codeGenerationFacade) {
		this.codeGenerationFacade = codeGenerationFacade;
		this.programParts = new ArrayList<>();
		this.variables = new HashMap<>();
		this.stack = new ArrayDeque<>();
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
	public void exitInt(JlangParser.IntContext ctx) {
		stack.push(new Value(ctx.getText(), VariableType.INTEGER_32));
	}

	@Override
	public void exitDouble(JlangParser.DoubleContext ctx) {
		stack.push(new Value(ctx.getText(), VariableType.DOUBLE));
	}

	@Override
	public void exitIntDeclaration(JlangParser.IntDeclarationContext ctx) {
		String variableName = ctx.ID().getText();
		programParts.add(codeGenerationFacade.declare(variableName, VariableType.INTEGER_32));
		variables.put(variableName, VariableType.INTEGER_32);
	}

	@Override
	public void exitRealDeclaration(JlangParser.RealDeclarationContext ctx) {
		String variableName = ctx.ID().getText();
		programParts.add(codeGenerationFacade.declare(variableName, VariableType.DOUBLE));
		variables.put(variableName, VariableType.DOUBLE);
	}

	@Override
	public void exitVariableDeclarationWithAssignment(
		JlangParser.VariableDeclarationWithAssignmentContext ctx
	) {
		var id = ctx.ID().getText();
		var value = stack.pop();
		programParts.add(codeGenerationFacade.declare(id, value.type()));
		programParts.add(codeGenerationFacade.assign(id, value.value(), value.type()));
		variables.put(id, value.type());
	}

	@Override
	public void exitVariableAssignment(JlangParser.VariableAssignmentContext ctx) {
		var id = ctx.ID().getText();
		var value = stack.pop();
		if (!variables.containsKey(id)) {
			errorsList.add(
				new CompilationLogicError("Variable " + id + " is not declared", ctx.start.getLine())
			);
			return;
		}
		if (variables.get(id) != value.type()) {
			errorsList.add(
				new CompilationLogicError(
					"Variable " +
					id +
					" is of type " +
					variables.get(id) +
					" but value is of type " +
					value.type(),
					ctx.start.getLine()
				)
			);
			return;
		}
		programParts.add(codeGenerationFacade.assign(id, value.value(), value.type()));
	}

	@Override
	public void exitAddition(JlangParser.AdditionContext ctx) {
		var right = stack.pop();
		var left = stack.pop();
		if (left.type() != right.type()) {
			errorsList.add(
				new CompilationLogicError(
					"Addition of different types: " + left.type() + " and " + right.type(),
					ctx.start.getLine()
				)
			);
			return;
		}
		var add = codeGenerationFacade.add(left.value(), right.value(), left.type());
		programParts.add(add._2());

		stack.push(new Value(add._1.value(), left.type()));
	}

	@Override
	public void exitSubtraction(JlangParser.SubtractionContext ctx) {
		var right = stack.pop();
		var left = stack.pop();
		if (left.type() != right.type()) {
			errorsList.add(
				new CompilationLogicError(
					"Subtraction of different types: " + left.type() + " and " + right.type(),
					ctx.start.getLine()
				)
			);
			return;
		}
		var add = codeGenerationFacade.sub(left.value(), right.value(), left.type());
		programParts.add(add._2());

		stack.push(new Value(add._1.value(), left.type()));
	}

	@Override
	public void exitMultiplication(JlangParser.MultiplicationContext ctx) {
		var right = stack.pop();
		var left = stack.pop();
		if (left.type() != right.type()) {
			errorsList.add(
				new CompilationLogicError(
					"Multiplication of different types: " + left.type() + " and " + right.type(),
					ctx.start.getLine()
				)
			);
			return;
		}
		var add = codeGenerationFacade.mul(left.value(), right.value(), left.type());
		programParts.add(add._2());

		stack.push(new Value(add._1.value(), left.type()));
	}

	@Override
	public void exitDivision(JlangParser.DivisionContext ctx) {
		var right = stack.pop();
		var left = stack.pop();
		if (left.type() != right.type()) {
			errorsList.add(
				new CompilationLogicError(
					"Division of different types: " + left.type() + " and " + right.type(),
					ctx.start.getLine()
				)
			);
			return;
		}
		var add = codeGenerationFacade.div(left.value(), right.value(), left.type());
		programParts.add(add._2());

		stack.push(new Value(add._1.value(), left.type()));
	}

	@Override
	public void exitVariable(JlangParser.VariableContext ctx) {
		var id = ctx.ID().getText();
		if (!variables.containsKey(id)) {
			errorsList.add(
				new CompilationLogicError("Variable " + id + " is not declared", ctx.start.getLine())
			);
			return;
		}
		var type = variables.get(id);
		var load = codeGenerationFacade.load(id, type);
		programParts.add(load._2());

		stack.push(new Value(load._1.value(), type));
	}

	@Override
	public void exitFunctionCall(JlangParser.FunctionCallContext ctx) {
		String functionName = ctx.ID().getText();
		List<Value> arguments = new ArrayList<>();
		for (JlangParser.Expression0Context ignored : ctx.argument_list().expression0()) {
			arguments.add(stack.pop());
		}
		Collections.reverse(arguments); // Reverse the arguments because they are in reverse order on the stack

		switch (functionName) {
			case "nazachodziejest":
				handlePrintFunction(arguments);
				break;
			case "lewarekazapraweucho":
				handleReadFunction(arguments);
				break;
			default:
				errorsList.add(
					new CompilationLogicError("Unknown function: " + functionName, ctx.start.getLine())
				);
		}
	}

	private void handlePrintFunction(List<Value> arguments) {
		for (Value argument : arguments) {
			String printfFormat =
				switch (argument.type()) {
					case INTEGER_32 -> "@.str.1";
					case DOUBLE -> "@.str";
				};
			final var printfCode = String.format(
				"call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* %s, i32 0, i32 0), %s %s)",
				printfFormat,
				argument.type().getLlvmVariableNameLiteral(),
				argument.value()
			);
			programParts.add(printfCode);
		}
	}

	private void handleReadFunction(List<Value> arguments) {
		for (Value argument : arguments) {
			if (!variables.containsKey(argument.value())) {
				errorsList.add(
					new CompilationLogicError("Variable " + argument.value() + " is not declared", -1)
				);
				return;
			}
			String scanfFormat =
				switch (argument.type()) {
					case INTEGER_32 -> "@.str.1";
					case DOUBLE -> "@.str";
				};
			String scanfCode =
				"call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* " +
				scanfFormat +
				", i32 0, i32 0), " +
				argument.type().getLlvmVariableNameLiteral() +
				"* " +
				argument.value() +
				")";
			programParts.add(scanfCode);
		}
	}

	public String getLLVMOutput() {
		return String.join("\n", programParts);
	}
}