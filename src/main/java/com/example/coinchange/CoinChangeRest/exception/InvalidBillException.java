package com.example.coinchange.CoinChangeRest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Exception thrown when an invalid bill value is provided in a request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBillException extends RuntimeException {
    /**
     * Constructs a new InvalidBillException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public InvalidBillException(String message) {
        super(message);
    }
}
