package com.example.Hospital_Management_System.exception;

import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex){
        String errorDetail= "Exceptoin:" + ex.getClass().getSimpleName()+"\n"+
                            "Message:"+ ex.getMessage()+"\n"+
                           "Cause:"+ ex.getCause();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> inputValidationExceptionHandler(MethodArgumentNotValidException ex){

        return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldErrors().stream().map(a->
                a.getField()+":"+ a.getDefaultMessage()
        ).collect(Collectors.toList())
        );
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex){
        String errorDetail= "Exceptoin:" + ex.getClass().getSimpleName()+"\n"+
                "Message:"+ ex.getMessage()+"\n"+
                "Cause:"+ ex.getCause();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessException ex){
        String errorDetail= "Exceptoin:" + ex.getClass().getSimpleName()+"\n"+
                "Message:"+ ex.getMessage()+"\n"+
                "Cause:"+ ex.getCause();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
    }
}
