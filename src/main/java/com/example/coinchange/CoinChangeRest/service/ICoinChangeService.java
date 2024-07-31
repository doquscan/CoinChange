package com.example.coinchange.CoinChangeRest.service;


import java.math.BigDecimal;
import java.util.Map;

public interface ICoinChangeService {
    /**
     * Gets the change for the given bill.
     *
     * @param bill The bill amount.
     * @return A map containing denominations and their respective counts for the change.
     * @throws IllegalArgumentException If the bill value is invalid.
     */
    Map<BigDecimal, Integer> getChange(int bill);
}