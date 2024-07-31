package com.example.coinchange.CoinChangeRest.service;
import com.example.coinchange.CoinChangeRest.entity.CoinEntity;
import com.example.coinchange.CoinChangeRest.repository.CoinRepository;
import com.example.coinchange.CoinChangeRest.service.CoinTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CoinTransactionServiceTest {

    @Mock
    private CoinRepository coinRepository;

    @InjectMocks
    private CoinTransactionService coinTransactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetChange_Success() {
        // Setup mock data
        List<CoinEntity> mockCoins = List.of(
                new CoinEntity(new BigDecimal("0.25"), 100),
                new CoinEntity(new BigDecimal("0.10"), 100),
                new CoinEntity(new BigDecimal("0.05"), 100),
                new CoinEntity(new BigDecimal("0.01"), 100)
        );

        Map<BigDecimal, Integer> change = Map.of(
                new BigDecimal("0.25"), 8,
                new BigDecimal("0.10"), 1,
                new BigDecimal("0.05"), 0,
                new BigDecimal("0.01"), 0
        );

        // Call the method
        Map<BigDecimal, Integer> result = coinTransactionService.getChange(2, mockCoins, change);

        // Verify interactions
        verify(coinRepository, times(4)).save(any(CoinEntity.class));

        // Assert the results
        assertNotNull(result);
        assertEquals(8, result.get(new BigDecimal("0.25")));
        assertEquals(1, result.get(new BigDecimal("0.10")));
    }

    @Test
    public void testUpdateCoinQuantities_Success() {
        // Setup mock data
        List<CoinEntity> mockCoins = List.of(
                new CoinEntity(new BigDecimal("0.25"), 100),
                new CoinEntity(new BigDecimal("0.10"), 100),
                new CoinEntity(new BigDecimal("0.05"), 100),
                new CoinEntity(new BigDecimal("0.01"), 100)
        );

        Map<BigDecimal, Integer> change = Map.of(
                new BigDecimal("0.25"), 8,
                new BigDecimal("0.10"), 1
        );

        // Call the method
        coinTransactionService.getChange(2, mockCoins, change);

        // Verify interactions
        verify(coinRepository, times(2)).save(any(CoinEntity.class));

        // Assert the updated quantities
        assertEquals(92, mockCoins.get(0).getQuantity()); // 100 - 8
        assertEquals(99, mockCoins.get(1).getQuantity()); // 100 - 1
    }

    @Test
    public void testUpdateCoinQuantities_InconsistentQuantity() {
        // Setup mock data
        List<CoinEntity> mockCoins = List.of(
                new CoinEntity(new BigDecimal("0.25"), 5) // Not enough coins
        );

        Map<BigDecimal, Integer> change = Map.of(
                new BigDecimal("0.25"), 10 // Requesting more than available
        );

        // Expect an IllegalStateException due to inconsistent quantity
        assertThrows(IllegalStateException.class, () -> {
            coinTransactionService.getChange(2, mockCoins, change);
        });

        // Verify that save is not called due to exception
        verify(coinRepository, never()).save(any(CoinEntity.class));
    }
}