package edu.miu.cs.cs544.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyEntityNotFoundException extends RuntimeException {
    public MyEntityNotFoundException(String message) {
        super(message);
    }
}