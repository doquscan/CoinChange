package com.example.coinchange.CoinChangeRest.service;//package com.example.change.CoinChangeREST.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.coinchange.CoinChangeRest.entity.CoinEntity;
import com.example.coinchange.CoinChangeRest.exception.InsufficientCoinsException;
import com.example.coinchange.CoinChangeRest.repository.CoinRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ChangeServiceTest {

    @Mock
    private CoinRepository coinRepository;

    @Mock
    private CoinTransactionService coinTransactionService;

    @InjectMocks
    private CoinChangeService coinChangeService;

    @Value("${valid.bills}")
    private String validBills;

    @Value("${coin.denominations}")
    private String coinDenominations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set up the properties
//        coinChangeService.validBills = validBills;
//        coinChangeService.coinDenominations = coinDenominations;
    }

    @Test
    @Disabled
    public void testGetChange_ValidBill() {
        List<CoinEntity> mockCoins = List.of(
                new CoinEntity(new BigDecimal("0.25"), 100),
                new CoinEntity(new BigDecimal("0.10"), 100),
                new CoinEntity(new BigDecimal("0.05"), 100),
                new CoinEntity(new BigDecimal("0.01"), 100)
        );

        when(coinRepository.findAllByOrderByDenominationDesc()).thenReturn(mockCoins);
        when(coinTransactionService.getChange(anyInt(), anyList(), anyMap())).thenAnswer(invocation -> invocation.getArgument(2));

        Map<BigDecimal, Integer> change = coinChangeService.getChange(2);
        assertNotNull(change);
        assertEquals(8, change.get(new BigDecimal("0.25")));
    }

    @Test
    @Disabled
    public void testGetChange_InvalidBill() {
        assertThrows(IllegalArgumentException.class, () -> coinChangeService.getChange(3));
    }

    @Test
    @Disabled
    public void testGetChange_InsufficientCoins() {
        List<CoinEntity> mockCoins = List.of(
                new CoinEntity(new BigDecimal("0.25"), 1),
                new CoinEntity(new BigDecimal("0.10"), 1),
                new CoinEntity(new BigDecimal("0.05"), 1),
                new CoinEntity(new BigDecimal("0.01"), 1)
        );

        when(coinRepository.findAllByOrderByDenominationDesc()).thenReturn(mockCoins);

        assertThrows(InsufficientCoinsException.class, () -> coinChangeService.getChange(5));
    }

    @Test
    @Disabled
    public void testGetChange_SufficientCoins() {
        List<CoinEntity> mockCoins = List.of(
                new CoinEntity(new BigDecimal("0.25"), 4),
                new CoinEntity(new BigDecimal("0.10"), 10),
                new CoinEntity(new BigDecimal("0.05"), 10),
                new CoinEntity(new BigDecimal("0.01"), 100)
        );

        when(coinRepository.findAllByOrderByDenominationDesc()).thenReturn(mockCoins);
        when(coinTransactionService.getChange(anyInt(), anyList(), anyMap())).thenAnswer(invocation -> invocation.getArgument(2));

        Map<BigDecimal, Integer> change = coinChangeService.getChange(2);
        assertNotNull(change);
        assertEquals(4, change.get(new BigDecimal("0.25")));
        assertEquals(10, change.get(new BigDecimal("0.10")));
        assertEquals(10, change.get(new BigDecimal("0.05")));
        assertEquals(100, change.get(new BigDecimal("0.01")));
    }
}