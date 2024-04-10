package com.jlang.dev;

/**
 * This exception is a dev exception thrown when a method is not implemented yet.
 */
public class UnimplementedException extends RuntimeException {

    /**
     * Constructs a new UnimplementedException with a default messages.
     */
    public UnimplementedException() {
        super("This method is not implemented yet.");
    }

    /**
     * Constructs a new UnimplementedException with a custom messages.
     *
     * @param message the custom messages
     */
    public UnimplementedException(String message) {
        super(message);
    }
}
