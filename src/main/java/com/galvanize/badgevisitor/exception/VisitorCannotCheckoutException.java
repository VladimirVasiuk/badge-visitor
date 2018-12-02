package com.galvanize.badgevisitor.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
public class VisitorCannotCheckoutException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorCannotCheckoutException.class);

    public VisitorCannotCheckoutException(String exception) {
        super(exception);
        LOGGER.warn("Visitor cannot make checkout: {}", exception);
    }
}
