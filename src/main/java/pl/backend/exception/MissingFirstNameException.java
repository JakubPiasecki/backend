package pl.backend.exception;

public class MissingFirstNameException extends RuntimeException {
    public MissingFirstNameException(String message) {
        super(message);
    }
}