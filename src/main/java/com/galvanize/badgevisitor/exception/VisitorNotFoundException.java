package com.galvanize.badgevisitor.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class VisitorNotFoundException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorNotFoundException.class);

    public VisitorNotFoundException(String exception) {
        super(exception);
        LOGGER.warn("Visitor not found exception: {}", exception);
    }
}
