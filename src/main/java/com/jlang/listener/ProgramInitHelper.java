package com.jlang.listener;

class ProgramInitHelper {

	/*
	 *  These are utility strings declared inside the llvm program.
	 *  These are "%lf" "%lf\n" "%d" and "%d\n" they'll be used in C-style
	 *  printf(...) implementation but for the correct data type (int/double).
	 */
	static final String PRINTF_AND_SCANF_UTILITY_STRINGS_DECLARATION =
		"""
                    @.str = private unnamed_addr constant [5 x i8] c"%lf\\0A\\00", align 1
                    @.str.1 = private unnamed_addr constant [4 x i8] c"%d\\0A\\00", align 1
                    @.str.3 = private unnamed_addr constant [4 x i8] c"%lf\\00", align 1
                    @.str.4 = private unnamed_addr constant [3 x i8] c"%d\\00", align 1
                    """.trim();
	/*
	 * Starts the main function in llvm.
	 */
	static final String MAIN_FUNCTION_START =
		"""
                    define i32 @main() #0 {
                    """.trim();
	/*
	 * Ends the main function in llvm with status success (0).
	 */
	static final String MAIN_FUNCTION_END =
		"""
                        ret i32 0
                    }
                    """.trim();
	/*
	 *  Declares printf and scanf functions inside llvm code.
	 */
	static final String PRINTF_SCANF_DECLERATIONS =
		"""
                            declare i32 @scanf(ptr noundef, ...) #1
                            declare i32 @printf(ptr noundef, ...) #1
                    """.trim();
}
