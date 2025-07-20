package com.svats.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class ControllerHelpers {

    /** HTTP Status Code Handlers */

    static <T> ResponseEntity<T> handleExceptionOptional(Supplier<? extends Optional<T>> supplier) {
        return handleExceptionOptional(supplier, HttpStatus.OK);
    }

    static <T> ResponseEntity<T> handleExceptionOptional(Supplier<? extends Optional<T>> supplier, HttpStatus status) {
        return handleException(() -> handleOptional(supplier.get(), status));
    }

    static <T> ResponseEntity<T> handleException(Supplier<? extends ResponseEntity<T>> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private static <T> ResponseEntity<T> handleOptional(Optional<T> tOpt, HttpStatus status) {
        if (tOpt.isPresent()) return new ResponseEntity<>(tOpt.get(), status);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
