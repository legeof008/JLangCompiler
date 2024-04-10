package com.jlang.error;

import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;

import java.util.List;

@NoArgsConstructor
public class AssertingErrorContext implements ErrorContext {

    @Override
    public void accept(String message) {
        Assertions.fail("Unexpected error reported by analysis: " + message);
    }

    @Override
    public List<String> getErrors() {
        return List.of(); // Won't matter since accept fails
    }
}
