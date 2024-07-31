package com.example.coinchange.CoinChangeRest.controller;

import com.example.coinchange.CoinChangeRest.dto.ChangeResponseDTO;
import com.example.coinchange.CoinChangeRest.service.ICoinChangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Controller for handling requests to get change for a given bill.
 */
@RestController
@RequestMapping("/api/change")
@Tag(name = "Coin Change Service", description = "API for getting change for a given bill")
public class ChangeController implements IChangeController{

    private static final Logger logger = LoggerFactory.getLogger(ChangeController.class);

    @Autowired
    private ICoinChangeService coinService;

    @PostMapping
    @Operation(summary = "Get change for a bill", description = "Returns the optimal change for a given bill amount.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Change calculated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChangeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid bill value",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Insufficient coins to provide change",
                    content = @Content)
    })
    public ResponseEntity<ChangeResponseDTO> getChange(@RequestParam int bill) {

            Map<BigDecimal, Integer> change = coinService.getChange(bill);
            ChangeResponseDTO responseDTO = new ChangeResponseDTO(change);
            logger.info("Audit Log: Change calculation requested for bill: {}, Change provided: {}", bill, change);

           return ResponseEntity.ok(responseDTO);

    }
}
