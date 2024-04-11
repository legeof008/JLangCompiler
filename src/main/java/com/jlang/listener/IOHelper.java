package com.jlang.listener;

import com.jlang.antlr.JlangParser;
import com.jlang.llvm.variables.VariableType;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class IOHelper {
	//
	//	static boolean onlyOneVariableHandlingRestrictionMet(JlangParser.Function_callContext ctx) {
	//		return ctx.argument_list().children.size() == 1;
	//	}
	//
	//	static boolean isStdPrintfOperation(JlangParser.Function_callContext ctx) {
	//		return Objects.equals(ctx.ID().getText(), "nazachodziemamy");
	//	}
	//
	//	static boolean isStdScanfOperation(JlangParser.Function_callContext ctx) {
	//		return Objects.equals(ctx.ID().getText(), "lewarekazapraweucho");
	//	}
	//
	//	static boolean allVariablesInArgumentsPreviouslyDeclared(
	//		final Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues,
	//		final JlangParser.Function_callContext ctx
	//	) {
	//		return ctx
	//			.argument_list()
	//			.children.stream()
	//			.filter(x -> !x.getClass().equals(TerminalNodeImpl.class))
	//			.allMatch(x -> declaredVariablesAndValues.containsKey(x.getText()));
	//	}
}
