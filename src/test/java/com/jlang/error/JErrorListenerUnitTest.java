package com.jlang.error;

import org.antlr.v4.runtime.Recognizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JErrorListenerUnitTest {

    @Mock
    private ErrorLoggingBackend errorLoggingBackend;

    @Mock
    private Recognizer<?, ?> recognizer;

    private JErrorListener jErrorListener;

    @BeforeEach
    void setUp() {
        jErrorListener = JErrorListener.builder()
                .errorLoggingBackend(errorLoggingBackend)
                .build();
    }

    @Test
    void shouldLogError() {
        // given
        // when
        jErrorListener.syntaxError(recognizer, null, 1, 1, "msg", null);

        // then
        verify(errorLoggingBackend).accept("line 1:1 msg");
    }
}