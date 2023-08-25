package pl.backend.exception;


public class MissingLastNameException extends RuntimeException {
    public MissingLastNameException(String message) {
        super(message);
    }
}