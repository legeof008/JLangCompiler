package com.jlang.listener;

import static com.jlang.listener.ProgramInitHelper.MAIN_FUNCTION_END;
import static com.jlang.listener.ProgramInitHelper.MAIN_FUNCTION_START;
import static com.jlang.listener.ProgramInitHelper.PRINTF_AND_SCANF_UTILITY_STRINGS_DECLARATION;
import static com.jlang.listener.ProgramInitHelper.PRINTF_SCANF_DECLERATIONS;

import com.jlang.antlr.JlangBaseListener;
import com.jlang.antlr.JlangParser;
import com.jlang.error.CompilationLogicError;
import com.jlang.language.Symbol;
import com.jlang.language.scope.Scope;
import com.jlang.llvm.LLVMGeneratorFacade;
import com.jlang.llvm.variables.Type;
import com.jlang.llvm.variables.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

public class JLangGeneratorListener extends JlangBaseListener {

	private final LLVMGeneratorFacade codeGenerationFacade;
	private final List<String> programParts;
	private final Scope globalScope;
	private Scope currentScope;
	private final List<Scope> localScopes;

	@Getter
	private final List<CompilationLogicError> errorsList;

	public JLangGeneratorListener(LLVMGeneratorFacade codeGenerationFacade) {
		this.codeGenerationFacade = codeGenerationFacade;
		this.programParts = new ArrayList<>();
		this.errorsList = new ArrayList<>();
		this.globalScope = Scope.global();
		this.currentScope = globalScope;
		this.localScopes = new ArrayList<>();
	}

	//TODO: Refactor all of the wierd usages of optional in methods below

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
		currentScope.pushValueOnStack(new Value(ctx.getText(), Type.INTEGER_32));
	}

	@Override
	public void exitDouble(JlangParser.DoubleContext ctx) {
		currentScope.pushValueOnStack(new Value(ctx.getText(), Type.DOUBLE));
	}

	@Override
	public void exitString(JlangParser.StringContext ctx) {
		currentScope.pushValueOnStack(new Value(ctx.getText(), Type.STRING));
	}
	@Override
	public void enterIntFunctionDeclaration(JlangParser.IntFunctionDeclarationContext ctx) {
		var functionName = ctx.ID().getText();
		programParts.add(codeGenerationFacade.declareIntFunction(functionName));
		if(currentScope.equals(globalScope)){
			currentScope.addSymbol(new Symbol(functionName,Type.INT_FUNCTION));
			currentScope = Scope.child(currentScope);
		}
		else {
			errorsList.add(new CompilationLogicError("Cannot declare functions within functions or limited scopes",-1));
		}
	}
	@Override
	public void exitIntFunctionDeclaration(JlangParser.IntFunctionDeclarationContext ctx) {


	}

	@Override
	public void enterScopeDecleration(JlangParser.ScopeDeclerationContext ctx) {

	}
	@Override
	public void exitScopeDecleration(JlangParser.ScopeDeclerationContext ctx) {
		currentScope = currentScope.getParent();
	}

	@Override
	public void exitFunctionDeclaration(JlangParser.FunctionDeclarationContext ctx) {
		programParts.add(codeGenerationFacade.endIntFunction());
	}

	@Override
	public void exitIntDeclaration(JlangParser.IntDeclarationContext ctx) {
		String variableName = ctx.ID().getText();
		programParts.add(codeGenerationFacade.declare(variableName, Type.INTEGER_32));
		currentScope.addSymbol(new Symbol(variableName, Type.INTEGER_32));
	}

	@Override
	public void exitRealDeclaration(JlangParser.RealDeclarationContext ctx) {
		String variableName = ctx.ID().getText();
		programParts.add(codeGenerationFacade.declare(variableName, Type.DOUBLE));
		currentScope.addSymbol(new Symbol(variableName, Type.DOUBLE));
	}

	@Override
	public void exitVariableDeclarationWithAssignment(
		JlangParser.VariableDeclarationWithAssignmentContext ctx
	) {
		var id = ctx.ID().getText();
		var value = currentScope.popValueFromStack();
		if (value.type() == Type.STRING) {
			programParts.addFirst(codeGenerationFacade.createConstantString(id, value.value()));
			programParts.add(codeGenerationFacade.declare(id, "[255 x i8]"));
			programParts.add(codeGenerationFacade.assign(id, id));
		} else {
			programParts.add(codeGenerationFacade.declare(id, value.type()));
			programParts.add(codeGenerationFacade.assign(id, value.value(), value.type()));
		}
		currentScope.addSymbol(new Symbol(id, value.type()));
	}

	@Override
	public void exitVariableAssignment(JlangParser.VariableAssignmentContext ctx) {
		var id = ctx.ID().getText();
		var value = currentScope.popValueFromStack();
		var symbol = currentScope.findSymbolInCurrentScope(id);
		if (symbol.isEmpty()) {
			errorsList.add(
				new CompilationLogicError("Variable " + id + " is not declared", ctx.start.getLine())
			);
			return;
		}
		if (symbol.get().type() != value.type()) {
			errorsList.add(
				new CompilationLogicError(
					"Variable " +
					id +
					" is of type " +
					symbol.get().type() +
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
		var right = currentScope.popValueFromStack();
		var left = currentScope.popValueFromStack();
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

		currentScope.pushValueOnStack(new Value(add._1.value(), left.type()));
	}

	@Override
	public void exitSubtraction(JlangParser.SubtractionContext ctx) {
		var right = currentScope.popValueFromStack();
		var left = currentScope.popValueFromStack();
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

		currentScope.pushValueOnStack(new Value(add._1.value(), left.type()));
	}

	@Override
	public void exitMultiplication(JlangParser.MultiplicationContext ctx) {
		var right = currentScope.popValueFromStack();
		var left = currentScope.popValueFromStack();
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

		currentScope.pushValueOnStack(new Value(add._1.value(), left.type()));
	}

	@Override
	public void exitDivision(JlangParser.DivisionContext ctx) {
		var right = currentScope.popValueFromStack();
		var left = currentScope.popValueFromStack();
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

		currentScope.pushValueOnStack(new Value(add._1.value(), left.type()));
	}

	@Override
	public void exitVariable(JlangParser.VariableContext ctx) {
		var id = ctx.ID().getText();
		var type = currentScope.findSymbolInCurrentScope(id).map(Symbol::type);
		if (type.isEmpty()) {
			errorsList.add(
				new CompilationLogicError("Variable " + id + " is not declared", ctx.start.getLine())
			);
			return;
		}

		var load =
			switch (type.get()) {
				case DOUBLE, INTEGER_32 -> codeGenerationFacade.load(id, type.get());
				case STRING -> codeGenerationFacade.loadString(id, 16, type.get());
				case null, default -> null;
			};

		programParts.add(load._2());

		currentScope.pushValueOnStack(new Value(load._1.value(), type.get()));
	}

	@Override
	public void exitFunctionCall(JlangParser.FunctionCallContext ctx) {
		String functionName = ctx.ID().getText();
		List<Value> arguments = new ArrayList<>();
		for (JlangParser.Expression0Context ignored : ctx.argument_list().expression0()) {
			arguments.add(currentScope.popValueFromStack());
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
				case INT_FUNCTION -> null;
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
		programParts.add(argument.type() == Type.STRING ? printfCodeForStrings : printfCode);
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
		if (currentScope.findSymbolInCurrentScope(argument.value()).isEmpty()) {
			errorsList.add(
				new CompilationLogicError("Variable " + argument.value() + " is not declared", -1)
			);
			return;
		}

		String scanfFormat =
			switch (currentScope.findSymbolInCurrentScope(argument.value()).get().type()) {
				case INTEGER_32 -> "@.str.4";
				case DOUBLE -> "@.str.3";
				case STRING -> ""; //TODO#20
				case INT_FUNCTION -> null;
			};
		final var scanfCode = String.format(
			"call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* %s, i32 0, i32 0), %s* %%%s)",
			scanfFormat,
			currentScope.findSymbolInCurrentScope(argument.value()).get().type().getLlvmVariableNameLiteral(),
			argument.value()
		);
		programParts.add(scanfCode);
		codeGenerationFacade.incrementRegistry();
	}

	@Override
	public void exitVariableAddress(JlangParser.VariableAddressContext ctx) {
		var id = ctx.ID().getText();
		if (currentScope.findSymbolInCurrentScope(id).isEmpty()) {
			errorsList.add(
				new CompilationLogicError("Variable " + id + " is not declared", ctx.start.getLine())
			);
			return;
		}

		// Instead of loading the variable into a register, we just push the variable name onto the stack
		currentScope.pushValueOnStack(new Value(id, currentScope.findSymbolInCurrentScope(id).get().type())); //TODO - this looks bad
	}

	public String getLLVMOutput() {
		return String.join("\n", programParts);
	}
}
