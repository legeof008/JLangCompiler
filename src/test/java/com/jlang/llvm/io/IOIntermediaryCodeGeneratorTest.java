package com.jlang.llvm.io;

import static org.assertj.core.api.Assertions.assertThat;

import com.jlang.llvm.variables.VariableType;
import org.junit.jupiter.api.Test;

class IOIntermediaryCodeGeneratorTest {

	private IOGenerator ioGenerator;
	private static final String PRINTF_GENERATED_CODE =
		"""
            %1 = load i32, i32* %variable_id
            %2 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strp, i32 0, i32 0), i32 %1
            """;

	private static final String SCANF_GENERATED_CODE =
		"""
            %2 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strs, i32 0, i32 0), i32* %another_variable_id
            """;

	@Test
	void shouldGeneratePrintfCodeAndIncrementRegistry() {
		//given
		ioGenerator = new IOIntermediaryCodeGenerator(1);
		//when
		var result = ioGenerator.printf("variable_id", VariableType.Integer32);
		//then
		assertThat(result).contains(PRINTF_GENERATED_CODE);
		assertThat(ioGenerator.getRegistry()).isEqualTo(3);
	}

	@Test
	void shouldGenerateScanfCodeAndIncrementRegistry() {
		//given
		ioGenerator = new IOIntermediaryCodeGenerator(2);
		//when
		var result = ioGenerator.scanf("another_variable_id", VariableType.Integer32);
		//then
		assertThat(result).contains(SCANF_GENERATED_CODE);
		assertThat(ioGenerator.getRegistry()).isEqualTo(3);
	}
}
