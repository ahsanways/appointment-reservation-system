package edu.miu.cs.cs544.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class MyErrorServiceAdvice {
	
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRunTimeException(RuntimeException e) {
        return error(INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler({MyEntityNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(MyEntityNotFoundException e) {
        return error(NOT_FOUND, e);
    }
    
    @ExceptionHandler({MyEntityAlreadyExistException.class})
    public ResponseEntity<String> handleAlreadyExistException(MyEntityAlreadyExistException e) {
        return error(INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<String> error(HttpStatus status, Exception e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body("Error: " + e.getMessage());
    }
}