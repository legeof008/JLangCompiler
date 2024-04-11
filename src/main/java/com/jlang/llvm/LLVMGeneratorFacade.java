package com.jlang.llvm;

import com.jlang.llvm.variables.Value;
import com.jlang.llvm.variables.VariableType;
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

	public String declare(String id, VariableType type) {
		return "%%%s = alloca %s".formatted(id, type.getLlvmVariableNameLiteral());
	}

	public String assign(String id, String value, VariableType type) {
		return "store %s %s, %s* %%%s".formatted(
				type.getLlvmVariableNameLiteral(),
				value,
				type.getLlvmVariableNameLiteral(),
				id
			);
	}

	/**
	 * Add two values
	 * @return Tuple2<Integer,String> where Integer is the registry and String is the LLVM code
	 */
	public Tuple2<Value, String> add(String valueLeft, String valueRight, VariableType type) {
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

	public Tuple2<Value, String> sub(String valueLeft, String valueRight, VariableType type) {
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

	public Tuple2<Value, String> mul(String valueLeft, String valueRight, VariableType type) {
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

	public Tuple2<Value, String> div(String valueLeft, String valueRight, VariableType type) {
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

	public Tuple2<Value, String> load(String id, VariableType type) {
		var load =
				Tuple.of(
						Value.atRegistry(registry, type),
						"%%%d = load %s, %s* %%%s".formatted(registry, type.getLlvmVariableNameLiteral(), type.getLlvmVariableNameLiteral(), id)
				);
		registry++;
		return load;
	}
}