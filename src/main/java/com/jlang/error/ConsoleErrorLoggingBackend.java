package com.jlang.error;

public class ConsoleErrorLoggingBackend implements ErrorLoggingBackend {
    @Override
    @SuppressWarnings("java:S106")
    public void accept(String message) {
        System.err.println(message);
    }
}
