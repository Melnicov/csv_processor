package com.gerimedica.processor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CSVException extends RuntimeException {

    private final HttpStatus httpStatus;

    public CSVException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
