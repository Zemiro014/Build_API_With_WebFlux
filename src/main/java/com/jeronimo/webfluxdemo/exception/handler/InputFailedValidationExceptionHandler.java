package com.jeronimo.webfluxdemo.exception.handler;

import com.jeronimo.webfluxdemo.dto.InputFailedValidationResponse;
import com.jeronimo.webfluxdemo.exception.InputFailedValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InputFailedValidationExceptionHandler {

    @ExceptionHandler(InputFailedValidationException.class)
    public ResponseEntity<InputFailedValidationResponse> handleException(InputFailedValidationException exception){
        InputFailedValidationResponse response = new InputFailedValidationResponse();
        response.setErrorCode(exception.getErrorCode());
        response.setInput(exception.getInput());
        response.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
