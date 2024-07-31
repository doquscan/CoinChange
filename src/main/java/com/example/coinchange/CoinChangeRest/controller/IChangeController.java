package com.example.coinchange.CoinChangeRest.controller;

import com.example.coinchange.CoinChangeRest.dto.ChangeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface IChangeController {
    /**
     * Endpoint to get change for a specified bill amount.
     *
     * @param bill The bill amount for which change is requested.
     * @return A response entity containing the change details.
     */
    ResponseEntity<ChangeResponseDTO> getChange(@RequestParam int bill);
}
