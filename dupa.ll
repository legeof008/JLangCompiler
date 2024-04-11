@.str = private unnamed_addr constant [5 x i8] c"%lf\0A\00", align 1
@.str.1 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c"%lf\00", align 1
@.str.4 = private unnamed_addr constant [3 x i8] c"%d\00", align 1
define i32 @main() #0 {
%x = alloca i32
%y = alloca double
%z = alloca i32
store i32 2, i32* %z
store i32 3, i32* %z
%1 = fadd double 2.5, 0.1
%2 = fmul double 1.0, 5.0
%3 = fsub double %1, %2
store double %3, double* %y
%4 = load double, double* %y
%5 = fmul double 2.5, %4
%u = alloca double
store double %5, double* %u
%6 = load double, double* %u
call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), double %6)
%8 = add i32 2, 2
call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i32 %8)
call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.3, i32 0, i32 0), i32* %z)
%11 = load i32, i32* %z
%12 = add i32 %11, 1
store i32 %12, i32* %z
%13 = load i32, i32* %z
call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i32 0, i32 0), i32 %13)
ret i32 0
}
declare i32 @scanf(ptr noundef, ...) #1
        declare i32 @printf(ptr noundef, ...) #1