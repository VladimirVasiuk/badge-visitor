package com.galvanize.badgevisitor.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VisitorNotFoundException.class)
    protected ResponseEntity<CustomException> handleVisitorNotFoundException(VisitorNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new CustomException(
                HttpStatus.NOT_FOUND.value(),
                ex.getLocalizedMessage(),
                request.getDescription(false)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VisitorNotCreatedException.class)
    protected ResponseEntity<CustomException> handleVisitorNotCreatedException(VisitorNotCreatedException ex, WebRequest request) {
        return new ResponseEntity<>(new CustomException(
                HttpStatus.EXPECTATION_FAILED.value(),
                ex.getLocalizedMessage(),
                request.getDescription(false)), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(VisitorCannotCheckoutException.class)
    protected ResponseEntity<CustomException> handleVisitorCannotCheckoutException(VisitorCannotCheckoutException ex, WebRequest request) {
        return new ResponseEntity<>(new CustomException(
                HttpStatus.EXPECTATION_FAILED.value(),
                ex.getLocalizedMessage(),
                request.getDescription(false)), HttpStatus.EXPECTATION_FAILED);
    }

    @Data
    @AllArgsConstructor
    private static class CustomException {
        private int error_code;
        private String message;
        private String uri;
    }
}

