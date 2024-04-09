package com.jlang.error;

public interface ErrorLoggingBackend {

    void accept(String message);
}
