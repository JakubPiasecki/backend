package pl.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<String> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MissingFirstNameException.class)
    public ResponseEntity<String> handleMissingFirstNameException(MissingFirstNameException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MissingLastNameException.class)
    public ResponseEntity<String> handleMissingLastNameException(MissingLastNameException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<String> handleSkillNotFoundException(SkillNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(SkillAlreadyExistsException.class)
    public ResponseEntity<String> handleSkillAlreadyExistsException(SkillAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
