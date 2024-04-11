package com.jlang.error;

import static org.mockito.Mockito.verify;

import org.antlr.v4.runtime.Recognizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JErrorListenerUnitTest {

	@Mock
	private ErrorContext errorContext;

	@Mock
	private Recognizer<?, ?> recognizer;

	private JErrorListener jErrorListener;

	@BeforeEach
	void setUp() {
		jErrorListener = JErrorListener.builder().errorContext(errorContext).build();
	}

	@Test
	void shouldLogError() {
		// given
		// when
		jErrorListener.syntaxError(recognizer, null, 1, 1, "msg", null);

		// then
		verify(errorContext).accept("line 1:1 msg");
	}
}
