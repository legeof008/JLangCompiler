package com.jlang.llvm;

import com.jlang.llvm.variables.Type;
import com.jlang.llvm.variables.Value;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class LLVMGeneratorFacade {

	private int registry;

	public LLVMGeneratorFacade() {
		this.registry = 1;
	}

	public String declare(String id, Type type) {
		return "%%%s = alloca %s".formatted(id, type.getLlvmVariableNameLiteral());
	}

	public String declareIntFunction(String id) {
		return "define i32 @" + id + "() nounwind {\n";
	}

	public String endIntFunction(String value) {
		// TODO: handle returning actual values
		return "ret i32 " + value + "\n}\n";
	}

	public String declareIntFunction(String id) {
		return "define i32 @" + id + "() nounwind {\n";
	}

	public String endIntFunction(String value) {
		// TODO: handle returning actual values
		return "ret i32 " + value + "\n}\n";
	}

	public String declare(String id, String type) {
		return "%%%s = alloca %s".formatted(id, type);
	}

	public String assign(String id, String value, Type type) {
		return "store %s %s, %s* %%%s".formatted(
				type.getLlvmVariableNameLiteral(),
				value,
				type.getLlvmVariableNameLiteral(),
				id
			);
	}

	public String createConstantString(String originalVariableId, String value) {
		return "@__const.main.str.%s = private unnamed_addr constant [255 x i8] c\"%s\"".formatted(
				originalVariableId,
				padString(value, 255)
			);
	}

	public String assign(String id, String constantId) {
		return "  call void @llvm.memcpy.p0.p0.i64(ptr align 8 %%%s, ptr align 8 @__const.main.str.%s, i64 255, i1 false)".formatted(
				id,
				constantId
			);
	}

	/**
	 * Add two values
	 * @return Tuple2<Integer,String> where Integer is the registry and String is the LLVM code
	 */
	public Tuple2<Value, String> add(String valueLeft, String valueRight, Type type) {
		final Tuple2<Value, String> ret;
		switch (type) {
			case INTEGER_32:
				ret =
					Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = add i32 %s, %s".formatted(registry, valueLeft, valueRight)
					);
				registry++;
				return ret;
			case DOUBLE:
				ret =
					Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = fadd double %s, %s".formatted(registry, valueLeft, valueRight)
					);
				registry++;
				return ret;
			default:
				throw new IllegalArgumentException("Unknown type: " + type);
		}
	}

	public Tuple2<Value, String> sub(String valueLeft, String valueRight, Type type) {
		final Tuple2<Value, String> ret;
		switch (type) {
			case INTEGER_32:
				ret =
					Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = sub i32 %s, %s".formatted(registry, valueLeft, valueRight)
					);
				registry++;
				return ret;
			case DOUBLE:
				ret =
					Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = fsub double %s, %s".formatted(registry, valueLeft, valueRight)
					);
				registry++;
				return ret;
			default:
				throw new IllegalArgumentException("Unknown type: " + type);
		}
	}

	public Tuple2<Value, String> mul(String valueLeft, String valueRight, Type type) {
		final Tuple2<Value, String> ret;
		switch (type) {
			case INTEGER_32:
				ret =
					Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = mul i32 %s, %s".formatted(registry, valueLeft, valueRight)
					);
				registry++;
				return ret;
			case DOUBLE:
				ret =
					Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = fmul double %s, %s".formatted(registry, valueLeft, valueRight)
					);
				registry++;
				return ret;
			default:
				throw new IllegalArgumentException("Unknown type: " + type);
		}
	}

	public Tuple2<Value, String> div(String valueLeft, String valueRight, Type type) {
		final Tuple2<Value, String> ret;
		switch (type) {
			case INTEGER_32:
				ret =
					Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = sdiv i32 %s, %s".formatted(registry, valueLeft, valueRight)
					);
				registry++;
				return ret;
			case DOUBLE:
				ret =
					Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = fdiv double %s, %s".formatted(registry, valueLeft, valueRight)
					);
				registry++;
				return ret;
			default:
				throw new IllegalArgumentException("Unknown type: " + type);
		}
	}

	public Tuple2<Value, String> load(String id, Type type) {
		var load = Tuple.of(
			Value.atRegistry(registry, type),
			"%%%d = load %s, %s* %%%s".formatted(
					registry,
					type.getLlvmVariableNameLiteral(),
					type.getLlvmVariableNameLiteral(),
					id
				)
		);
		registry++;
		return load;
	}

	public Tuple2<Value, String> loadString(String id, int stringLength, Type type) {
		var load = Tuple.of(
			Value.atRegistry(registry, type),
			"%%%d = getelementptr inbounds [%d x i8], [%d x i8]* %%%s, i64 0, i64 0".formatted(
					registry,
					stringLength,
					stringLength,
					id
				)
		);
		registry++;
		return load;
	}

	public static String padString(String input, int length) {
		if (input.length() >= length) {
			return input.substring(0, length);
		} else {
			return input + "\\00".repeat(length - input.length());
		}
	}

	public void incrementRegistry() {
		registry++;
	}
}
