package edu.miu.cs.cs544.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyEntityAlreadyExistException extends RuntimeException {
    public MyEntityAlreadyExistException(String message) {
        super(message);
    }
}