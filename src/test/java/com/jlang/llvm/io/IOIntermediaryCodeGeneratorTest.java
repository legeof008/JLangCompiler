package com.jlang.llvm.io;

import static org.assertj.core.api.Assertions.assertThat;

import com.jlang.llvm.variables.VariableType;
import org.junit.jupiter.api.Test;

class IOIntermediaryCodeGeneratorTest {

	private IOGenerator ioGenerator;
	private static final String PRINTF_GENERATED_CODE =
		"""
            %1 = load i32, ptr %variable_id
            %2 = call i32 (ptr, ...) @printf(ptr noundef @.str.3, i32 %1)
            """.trim();

	private static final String SCANF_GENERATED_CODE =
		"""
            %2 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef @.str.3, ptr noundef %another_variable_id)
            """.trim();

	@Test
	void shouldGeneratePrintfCodeAndIncrementRegistry() {
		//given
		ioGenerator = new IOIntermediaryCodeGenerator();
		//when
		var result = ioGenerator.printf("variable_id", VariableType.Integer32, 1 );
		//then
		assertThat(result).contains(PRINTF_GENERATED_CODE);
	}

	@Test
	void shouldGenerateScanfCodeAndIncrementRegistry() {
		//given
		ioGenerator = new IOIntermediaryCodeGenerator();
		//when
		var result = ioGenerator.scanf("another_variable_id", VariableType.Integer32, 2);
		//then
		assertThat(result).contains(SCANF_GENERATED_CODE);
	}
}
