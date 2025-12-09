package org.skypro.skyshop.controller.advice;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.error.ShopError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShopControllerAdvice {

    @ExceptionHandler(NoSuchProductException.class)
    public ResponseEntity<ShopError> handleNoSuchProductException(NoSuchProductException ex) {
        ShopError error = new ShopError(
                "PRODUCT_NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // HTTP 404
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ShopError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ShopError error = new ShopError(
                "INVALID_ARGUMENT",
                "Некорректные параметры запроса: " + ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // HTTP 400
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ShopError> handleGeneralException(Exception ex) {
        ShopError error = new ShopError(
                "INTERNAL_SERVER_ERROR",
                "Внутренняя ошибка сервера"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500
                .body(error);
    }
}