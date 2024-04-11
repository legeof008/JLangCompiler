package com.jlang.error;

import java.util.List;

public interface ErrorContext {
	void accept(String message);

	/**
	 * @return list of errors
	 * @implNote This method should return unmodifiable list
	 */
	List<String> getErrors();
}
