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
	public void exitString(JlangParser.StringContext ctx) {
		stack.push(new Value(ctx.getText(), VariableType.STRING));
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
		if (value.type() == VariableType.STRING) {
			programParts.addFirst(codeGenerationFacade.createConstantString(id, value.value()));
			programParts.add(codeGenerationFacade.declare(id, "[255 x i8]"));
			programParts.add(codeGenerationFacade.assign(id, id));
		} else {
			programParts.add(codeGenerationFacade.declare(id, value.type()));
			programParts.add(codeGenerationFacade.assign(id, value.value(), value.type()));
		}
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

		var load = switch (type) {
			case DOUBLE, INTEGER_32 -> codeGenerationFacade.load(id, type);
			case STRING -> codeGenerationFacade.loadString(id, 16, type);
		};

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
		if (arguments.size() != 1) {
			errorsList.add(
				new CompilationLogicError("The nazachodziejest function accepts only one argument", -1)
			);
			return;
		}

		Value argument = arguments.getFirst();
		String printfFormat =
			switch (argument.type()) {
				case INTEGER_32 -> "@.str.1";
				case DOUBLE -> "@.str";
				case STRING -> "@.str.5";
			};
		final var printfCodeForStrings = String.format(
				"call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([255 x i8], [255 x i8]* %s, i32 0, i32 0), i8* %s)",
				printfFormat,
				argument.value()
		);
		final var printfCode = String.format(
			"call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* %s, i32 0, i32 0), %s %s)",
			printfFormat,
			argument.type().getLlvmVariableNameLiteral(),
			argument.value()
		);
		programParts.add(argument.type() == VariableType.STRING ? printfCodeForStrings : printfCode);
		codeGenerationFacade.incrementRegistry();
	}

	private void handleReadFunction(List<Value> arguments) {
		if (arguments.size() != 1) {
			errorsList.add(
				new CompilationLogicError("The lewarekazapraweucho function accepts only one argument", -1)
			);
			return;
		}

		Value argument = arguments.getFirst();
		if (!variables.containsKey(argument.value())) { // this fails
			errorsList.add(
				new CompilationLogicError("Variable " + argument.value() + " is not declared", -1)
			);
			return;
		}

		String scanfFormat =
			switch (variables.get(argument.value())) {
				case INTEGER_32 -> "@.str.4";
				case DOUBLE -> "@.str.3";
				case STRING -> "";
			};
		final var scanfCode = String.format(
			"call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* %s, i32 0, i32 0), %s* %%%s)",
			scanfFormat,
			variables.get(argument.value()).getLlvmVariableNameLiteral(),
			argument.value()
		);
		programParts.add(scanfCode);
		codeGenerationFacade.incrementRegistry();
	}

	@Override
	public void exitVariableAddress(JlangParser.VariableAddressContext ctx) {
		var id = ctx.ID().getText();
		if (!variables.containsKey(id)) {
			errorsList.add(
				new CompilationLogicError("Variable " + id + " is not declared", ctx.start.getLine())
			);
			return;
		}

		// Instead of loading the variable into a register, we just push the variable name onto the stack
		stack.push(new Value(id, variables.get(id)));
	}

	public String getLLVMOutput() {
		return String.join("\n", programParts);
	}
}
