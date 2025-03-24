package test.schedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비밀번호 일치 검사
    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<?> handlePasswordMisMatchException(PasswordMisMatchException ex) {
        String response = ex.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    // Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidException(MethodArgumentNotValidException ex) {
       FieldError fieldError = ex.getBindingResult().getFieldError();
       String message = fieldError.getDefaultMessage();
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    // PathVariable
    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<?> handleScheduleNotFoundException(ScheduleNotFoundException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

}
