package com.jlang.listener;

import com.jlang.antlr.JlangBaseListener;
import com.jlang.antlr.JlangParser;
import com.jlang.llvm.LLVMGeneratorFacade;
import com.jlang.llvm.variables.VariableType;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jlang.listener.ArithmeticHelper.hasVariableTokensinsideArithmeticExpression;
import static com.jlang.listener.ArithmeticHelper.setVariablesInExpression;

public class JLangGeneratorListener extends JlangBaseListener {
    private LLVMGeneratorFacade codeGenerationFacade;
    private List<String> programParts;
    private Map<String, Map.Entry<String, VariableType>> declaredVariablesAndValues;
    /*
     *  These are utility strings declared inside the llvm program.
     *  These are "%lf" "%lf\n" "%d" and "%d\n" they'll be used in C-style
     *  printf(...) implementation but for the correct data type (int/double).
     */
    private static final String PRINTF_AND_SCANF_UTILITY_STRINGS_DECLARATION =
            """
                    @.str = private unnamed_addr constant [4 x i8] c"%lf\\00", align 1
                    @.str.1 = private unnamed_addr constant [5 x i8] c"%lf\\0A\\00", align 1
                    @.str.2 = private unnamed_addr constant [3 x i8] c"%d\\00", align 1
                    @.str.3 = private unnamed_addr constant [4 x i8] c"%d\\0A\\00", align 1                        
                    """.trim();
    /*
     * Starts the main function in llvm.
     */
    private static final String MAIN_FUNCTION_START =
            """
                    define i32 @main() #0 {
                    """.trim();
    /*
     * Ends the main function in llvm with status success (0).
     */
    private static final String MAIN_FUNCTION_END =
            """
                        ret i32 0
                    }
                    """.trim();
    /*
     *  Declares printf and scanf functions inside llvm code.
     */
    private static final String PRINTF_SCANF_DECLERATIONS =
            """
                            declare i32 @__isoc99_scanf(ptr noundef, ...) #1
                            declare i32 @printf(ptr noundef, ...) #1
                    """.trim();

    public JLangGeneratorListener(LLVMGeneratorFacade codeGenerationFacade) {
        this.codeGenerationFacade = codeGenerationFacade;
        this.programParts = new ArrayList<>();
        this.declaredVariablesAndValues = new HashMap<>();
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
        //TODO make this more general for number types
        declaredVariablesAndValues.put(ctx.ID().getText(), Map.entry("UNKNOWN", VariableType.Integer32));
        programParts.add(codeGenerationFacade.declare(ctx.ID().getText(), VariableType.Integer32));
    }

    @Override
    public void enterArithmetic_assignment(JlangParser.Arithmetic_assignmentContext ctx) {
        if (hasVariableTokensinsideArithmeticExpression(declaredVariablesAndValues, ctx)) {
            var expression = new ExpressionBuilder(ctx.getText())
                    .variables(declaredVariablesAndValues.keySet())
                    .build();
            declaredVariablesAndValues.keySet().forEach(
                    setVariablesInExpression(declaredVariablesAndValues, expression)
            );
            addLineDependingOnDataType(ctx, expression);
        } else {
            var expression = new ExpressionBuilder(ctx.arithmetic_statement().getText()).build();
            addLineDependingOnDataType(ctx, expression);
        }

    }

    private void addLineDependingOnDataType(JlangParser.Arithmetic_assignmentContext ctx, Expression expression) {
        switch (declaredVariablesAndValues.get(ctx.ID().getText()).getValue()) {
            case Double ->
                    programParts.add(codeGenerationFacade.assign(ctx.ID().getText(), "%f".formatted(expression.evaluate())));
            case Integer32 ->
                    programParts.add(codeGenerationFacade.assign(ctx.ID().getText(), "%d".formatted(Math.round(expression.evaluate()))));
        }
    }

    public String getLLVMOutput() {
        return String.join("\n", programParts);
    }
}
