package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.infrastructure.utils.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException exception) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(exception));
    }
}
