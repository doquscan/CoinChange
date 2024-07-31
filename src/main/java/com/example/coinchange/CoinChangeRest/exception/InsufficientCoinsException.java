package com.example.coinchange.CoinChangeRest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Exception thrown when there are insufficient coins to provide the requested change.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientCoinsException extends RuntimeException {
    /**
     * Constructs a new InsufficientCoinsException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public InsufficientCoinsException(String message) {
        super(message);
    }
}
