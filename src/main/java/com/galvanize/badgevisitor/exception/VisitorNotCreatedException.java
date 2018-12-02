package com.galvanize.badgevisitor.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
public class VisitorNotCreatedException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorNotCreatedException.class);

    public VisitorNotCreatedException(String exception) {
        super(exception);
        LOGGER.warn("Visitor was not created: {}", exception);
    }
}
