package com.jlang.error;

import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;

@NoArgsConstructor
public class AssertingErrorLoggingBackend implements ErrorLoggingBackend {

    @Override
    public void accept(String message) {
        Assertions.fail("Unexpected error reported by analysis: " + message);
    }
}
