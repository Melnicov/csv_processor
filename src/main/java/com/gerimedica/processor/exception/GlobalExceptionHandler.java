package com.gerimedica.processor.exception;

import com.gerimedica.processor.model.CSVResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CSVException.class)
    public ResponseEntity<CSVResponse<String>> handleCSVException(CSVException ex) {
        log.error("Failed to upload csv file", ex);
        return ResponseEntity.status(ex.getHttpStatus())
                             .body(CSVResponse.<String>builder()
                                              .response("Failed to process provided csv file.")
                                              .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CSVResponse<String>> handleException(Exception ex) {
        log.error("Failed to upload csv file", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(CSVResponse.<String>builder()
                                              .response("Failed to process provided csv file.")
                                              .build());
    }
}
