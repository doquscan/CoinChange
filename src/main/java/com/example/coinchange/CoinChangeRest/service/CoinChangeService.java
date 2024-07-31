package com.example.coinchange.CoinChangeRest.service;


import com.example.coinchange.CoinChangeRest.entity.AuditLog;
import com.example.coinchange.CoinChangeRest.entity.CoinEntity;
import com.example.coinchange.CoinChangeRest.exception.InsufficientCoinsException;
import com.example.coinchange.CoinChangeRest.repository.AuditLogRepository;
import com.example.coinchange.CoinChangeRest.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service to handle the logic of selecting sufficient coins
 */
@Service
public class CoinChangeService implements ICoinChangeService{

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private CoinTransactionService coinTransactionService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Value("${valid.bills}")
    private String validBills;

    @Value("${coin.denominations}")
    private String coinDenominations;
    /**
     * Gets the change for the given bill.
     *
     * @param bill The bill amount.
     * @return A map containing denominations and their respective counts for the change.
     * @throws IllegalArgumentException If the bill value is invalid.
     */
    public Map<BigDecimal, Integer> getChange(int bill) {
        BigDecimal amount = BigDecimal.valueOf(bill);
        if (!isValidBill(bill)) {
            throw new IllegalArgumentException("Invalid bill value.");
        }
        List<CoinEntity> coins = coinRepository.findAllByOrderByDenominationDesc();

        List<BigDecimal> denominations = getCoinDenominations();
        Map<BigDecimal, Integer> change = calculateChange(amount, coins, denominations);

        // Log audit data to the database
        String changeDetails = change.toString();
        AuditLog auditLog = new AuditLog(bill, changeDetails, LocalDateTime.now());
        auditLogRepository.save(auditLog);

        return coinTransactionService.getChange(bill, coins, change);
    }
    /**
     * Checks if the given bill is valid.
     *
     * @param bill The bill amount.
     * @return True if the bill is valid, false otherwise.
     */
    private boolean isValidBill(int bill) {
        // Split the validBills string and convert it to a list of integers
        List<Integer> validBillList = Arrays.stream(validBills.split(","))
                .map(Integer::parseInt)
                .toList();
        return validBillList.contains(bill);
    }
    /**
     * Gets the list of valid coin denominations.
     *
     * @return A list of valid coin denominations.
     */
    private List<BigDecimal> getCoinDenominations() {
        return Arrays.stream(coinDenominations.split(","))
                .map(BigDecimal::new)
                .collect(Collectors.toList());
    }
    /**
     * *****************************************************
     * Method: calculateChange
     * -----------------------------------------------------
     * Calculates the change for the given amount using the available coins.
     * Guarantees an optimal solution for this certain problem statement based in efficient way (faster)
     * Ensures that the least number of coins is used and the coin inventory constraints are respected.
     * *****************************************************
     *
    /**
     * Calculates the change for the given amount using the available coins.
     *
     * @param amount The amount for which change is needed.
     * @param coins The available coins.
     * @param denominations The denominations to use for change.
     * @return A map of denominations and their respective counts for the change.
     * @throws InsufficientCoinsException If there are not enough coins to provide the change.
     */
    private Map<BigDecimal, Integer> calculateChange(BigDecimal amount, List<CoinEntity> coins, List<BigDecimal> denominations) {
        Map<BigDecimal, Integer> change = new HashMap<>();
        BigDecimal remainingAmount = amount;

        Map<BigDecimal, Integer> coinInventory = coins.stream()
                .collect(Collectors.toMap(CoinEntity::getDenomination, CoinEntity::getQuantity));

        for (BigDecimal coinsdenomination : denominations) {
            int quantity = coinInventory.get(coinsdenomination);
            int coinsNeeded = remainingAmount.divide(coinsdenomination, BigDecimal.ROUND_DOWN).intValue();
            // Check if we have enough quantity and coin denomination
            if(coinsNeeded > 0 && quantity > 0 ) {
                int coinsToGive = Math.min(coinsNeeded, quantity);
                change.put(coinsdenomination, coinsToGive);
                // calculate how much we need and substract get the remaining.
                remainingAmount = remainingAmount.subtract(coinsdenomination.multiply(BigDecimal.valueOf(coinsToGive)));
            }
            if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) break;
        }
        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            throw new InsufficientCoinsException("Insufficient coins to provide change.");
        }

        return change;
    }


    /**
     * *****************************************************
     * *****************************************************
     * The minimum amount of coin selection logic Implementation with for possible dataSet increase for better performance.
     * Guarantees an optimal solution & reuses solutions to overlapping subproblems.
     *
     * <p>
     * Tabulation method (bottom-up approach) TC = O(n * amount) && SC = O(amount)
     * This implementation provides a more robust and efficient solution for the coin change problem using dynamic programming,
     * ensuring that the least number of coins is used and the coin inventory constraints are respected.
     * </p>
     *
     * <p>
     * Note: Ensure that the denominations and amounts are handled consistently as integers
     * (e.g., representing dollars as cents) to avoid precision issues with floating-point numbers.
     * P.S. You may need to change precisions on certain denominations algorithm to properly update values.
     * </p>
     *
     * @param amount        The amount for which change is needed.
     * @param coins         The available coins.
     * @param denominations The denominations to use for change.
     * @return A map of denominations and their respective counts for the change.
     * @throws InsufficientCoinsException If there are not enough coins to provide the change.
     * *****************************************************
     * *****************************************************
     */

    /**
     *   private Map<BigDecimal, Integer> calculateChange(BigDecimal amount, List<CoinEntity> coins, List<BigDecimal> denominations) {
     *         // Use a 2D array to keep track of the minimum number of coins needed to make each amount up to the target amount.
     *         Map<BigDecimal, Integer> change = new HashMap<>();
     *         //The buttomUp array represents the minimum number of coins needed to achieve each amount from 0 to amount
     *         BigDecimal[] buttomUp = new BigDecimal[amount.multiply(new BigDecimal("100")).intValue() + 1];
     *         //coinCount keeps track of the denomination used for each amount.
     *         int[] coinCount = new int[amount.multiply(new BigDecimal("100")).intValue() + 1];
     *
     *         // Initialize dp array with a high value indicating an impossible state
     *         buttomUp[0] = BigDecimal.ZERO;
     *
     *         for (int i = 1; i <= amount.multiply(new BigDecimal("100")).intValue(); i++) {
     *             buttomUp[i] = BigDecimal.valueOf(Integer.MAX_VALUE);
     *         }
     *
     *         // Map coin denominations to their respective quantities
     *         Map<BigDecimal, Integer> coinInventory = coins.stream()
     *                 .collect(Collectors.toMap(CoinEntity::getDenomination, CoinEntity::getQuantity));
     *         //For each coin denomination, update the dp array if using that denomination leads to a solution with fewer coins than previously recorded.
     *         for (BigDecimal denomination : denominations) {
     *             int denomValue = denomination.multiply(new BigDecimal("100")).intValue();
     *             // Quantity is decremented to ensure that the available coin limit is respected.
     *             int quantity = coinInventory.getOrDefault(denomination, 0);
     *
     *             for (int i = denomValue; i < buttomUp.length; i++) {
     *                 if (buttomUp[i - denomValue].compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) != 0 && quantity > 0) {
     *                     if (buttomUp[i - denomValue].add(denomination).compareTo(buttomUp[i]) < 0) {
     *                         buttomUp[i] = buttomUp[i - denomValue].add(denomination);
     *                         coinCount[i] = denomValue;
     *                         quantity--;
     *                     }
     *                 }
     *             }
     *         }
     *
     *         BigDecimal remainingAmount = amount.multiply(new BigDecimal("100"));
     *         //If dp[amount] is still set to a high value
     *         // (indicating it is not possible to form the amount with the available coins),throw an InsufficientCoinsException
     *         if (buttomUp[remainingAmount.intValue()].compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) == 0) {
     *             throw new InsufficientCoinsException("Insufficient coins to provide change.");
     *         }
     *
     *         // Backtrack to find the change denominations
     *         //After filling the dp array, backtrack to determine the specific denominations used to achieve the minimum number of coins.
     *         while (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
     *             //The coinCount array helps in backtracking to find the denominations and quantities used.
     *             int denomValue = coinCount[remainingAmount.intValue()];
     *             BigDecimal denom = new BigDecimal(denomValue).divide(new BigDecimal("100"));
     *             change.put(denom, change.getOrDefault(denom, 0) + 1);
     *             remainingAmount = remainingAmount.subtract(denom.multiply(new BigDecimal("100")));
     *         }
     *
     *         return change;
     *     }
     */


}

