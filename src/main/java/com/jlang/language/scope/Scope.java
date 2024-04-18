package com.jlang.language.scope;

import com.jlang.language.Symbol;
import com.jlang.llvm.variables.Value;
import io.vavr.control.Option;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Scope {

	private final Scope parent; //TODO when we implement /better/ scoping

	@Getter(value = AccessLevel.PRIVATE)
	private final Deque<Value> stack = new ArrayDeque<>();

	@Getter(value = AccessLevel.PRIVATE)
	private final Set<Symbol> symbols = new HashSet<>();

	public static Scope global() {
		return new Scope(null);
	}

	/**
	 * A child scope should have access to data from all the scopes 'above' itself.
	 * Therefore a child scope copies all the scope data from its parent.
	 *
	 * @param parent The parent scope from which all symbols and values are copied from
	 * @return a new scope with copied scope data
	 */
	public static Scope child(Scope parent) {
		var childScope = new Scope(parent);
		childScope.stack.addAll(parent.stack);
		childScope.symbols.addAll(parent.symbols);
		return childScope;
	}

	public static Scope childNoCopy(Scope parent) {
		var childScope = new Scope(parent);
		return childScope;
	}

	/**
	 * Pushes the specified value onto the stack.
	 *
	 * @param value the value to push
	 * @throws IllegalStateException if the element cannot be added at this
	 *         time due to capacity restrictions
	 * @throws ClassCastException if the class of the specified element
	 *         prevents it from being added to this deque
	 * @throws NullPointerException if the specified element is null and this
	 *         deque does not permit null elements
	 * @throws IllegalArgumentException if some property of the specified
	 *         element prevents it from being added to this deque
	 */
	public void pushValueOnStack(Value value) {
		stack.push(value);
	}

	/**
	 * Pops an element from the stack.
	 *
	 * @return the top of the stack
	 *
	 * @throws NoSuchElementException if this deque is empty
	 */
	public Value popValueFromStack() {
		return stack.pop();
	}

	/**
	 * Adds a symbol to the current scope.
	 *
	 * @param name the name of the symbol
	 *
	 * @throws NullPointerException if the specified name is null
	 */
	public Option<Symbol> findSymbolInCurrentScope(@NonNull String name) {
		final var symbol = symbols.stream().filter(s -> s.name().equals(name)).findFirst();
		return Option.ofOptional(symbol);
	}

	/**
	 * Adds a symbol to the current scope.
	 *
	 * @param symbol the symbol to add
	 *
	 * @throws NullPointerException if the specified symbol is null
	 * @throws ClassCastException if the class of the specified element
	 *         prevents it from being added
	 * @throws IllegalArgumentException if some property of the specified element
	 *         prevents it from being added
	 */
	public void addSymbol(@NonNull Symbol symbol) {
		symbols.add(symbol);
	}

	public Scope getParent() {
		final boolean isGlobal = parent == null;
		if (isGlobal) {
			return this;
		}
		return parent;
	}
}
