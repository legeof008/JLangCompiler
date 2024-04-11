package com.jlang.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConsoleErrorContext implements ErrorContext {

	private final List<String> errors = new ArrayList<>();

	@Override
	@SuppressWarnings("java:S106")
	public void accept(String message) {
		errors.add(message);
		System.err.println(message);
	}

	@Override
	public List<String> getErrors() {
		return Collections.unmodifiableList(errors);
	}
}
