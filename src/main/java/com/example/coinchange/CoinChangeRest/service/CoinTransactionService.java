package com.example.coinchange.CoinChangeRest.service;

import com.example.coinchange.CoinChangeRest.entity.CoinEntity;
import com.example.coinchange.CoinChangeRest.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Ensures that transaction management is correctly applied,
//preventing issues related to method self-invocation and ensuring data consistency
/**
 * Service to handle the logic of providing change and maintaining the state of available coins.
 */
@Service
public class CoinTransactionService {

    @Autowired
    private CoinRepository coinRepository;

    //Ensures that the operations are executed within a transaction. Ensuring data consistency.
    @Transactional
    public Map<BigDecimal, Integer> getChange(int bill, List<CoinEntity> coins, Map<BigDecimal, Integer> change) {
        // Update coin quantities in the database
        updateCoinQuantities(change, coins);
        return change;
    }
    /*
    To ensure that the coin quantities are accurately deducted and updated in the database
    once the available amount of coins is determined and the response is returned,
    We need to handle the database update operation.
    1- Retrieving the current state of the coin inventory.
    2- Calculating the change using the available inventory.
    3- Updating the database with the new quantities after the response is generated
  */
    /**
     * Updates the quantities of coins in the database based on the provided change.
     *
     * @param change The map of denominations and their respective counts for the change.
     * @param coins The available coins.
     */
    private void updateCoinQuantities(Map<BigDecimal, Integer> change, List<CoinEntity> coins) {
        Map<BigDecimal, CoinEntity> coinMap = coins.stream()
                .collect(Collectors.toMap(CoinEntity::getDenomination, coin -> coin));

        change.forEach((denomination, quantity) -> {
            CoinEntity coin = coinMap.get(denomination);
            if (coin != null) {
                int newQuantity = coin.getQuantity() - quantity;
                if (newQuantity < 0) {
                    throw new IllegalStateException("Inconsistent coin quantity update.");
                }
                coin.setQuantity(newQuantity);
                coinRepository.save(coin);
            }
        });
    }
}
