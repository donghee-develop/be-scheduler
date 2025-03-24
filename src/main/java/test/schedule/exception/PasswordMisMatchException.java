package test.schedule.exception;

import org.springframework.core.NestedCheckedException;

import java.io.UncheckedIOException;

public class PasswordMisMatchException extends RuntimeException{
    public PasswordMisMatchException() {
        super("Password mismatch");
    }
}
