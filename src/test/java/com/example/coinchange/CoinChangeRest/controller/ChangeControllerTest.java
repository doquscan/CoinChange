package com.example.coinchange.CoinChangeRest.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.coinchange.CoinChangeRest.exception.InsufficientCoinsException;
import com.example.coinchange.CoinChangeRest.exception.InvalidBillException;
import com.example.coinchange.CoinChangeRest.service.CoinChangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class ChangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CoinChangeService coinChangeService;

    @InjectMocks
    private ChangeController changeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(changeController)
                .setControllerAdvice(new GlobalExceptionHandler())  // Set the exception handler
                .build();
    }

    @Test
    public void testGetChange_Success() throws Exception {
        Map<BigDecimal, Integer> changeMap = Map.of(
                new BigDecimal("0.25"), 8
        );
        when(coinChangeService.getChange(2)).thenReturn(changeMap);

        mockMvc.perform(post("/api/change")
                        .param("bill", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.change['0.25']").value(8));
    }

    @Test
    public void testGetChange_InvalidBill() throws Exception {
        doThrow(new InvalidBillException("Invalid bill value")).when(coinChangeService).getChange(3);

        mockMvc.perform(post("/api/change")
                        .param("bill", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid bill value"));
    }

    @Test
    public void testGetChange_InsufficientCoins() throws Exception {
        doThrow(new InsufficientCoinsException("Insufficient coins to provide change."))
                .when(coinChangeService).getChange(100);

        mockMvc.perform(post("/api/change")
                        .param("bill", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Insufficient coins to provide change."));
    }

    @Test
    public void testGetChange_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(coinChangeService).getChange(5);

        mockMvc.perform(post("/api/change")
                        .param("bill", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}