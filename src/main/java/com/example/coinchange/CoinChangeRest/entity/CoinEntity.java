package com.example.coinchange.CoinChangeRest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


//@Data
@Entity
@Table(name="coin_inventory")
public class CoinEntity {

    @Id
    private BigDecimal denomination;
    private int quantity;

    public CoinEntity() {
        // Default constructor for JPA
    }
    /**
     * Constructor to create a CoinEntity with specified denomination and quantity.
     *
     * @param denomination The denomination of the coin.
     * @param quantity The number of coins of this denomination.
     */
    public CoinEntity(BigDecimal denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
    }
    /**
     * Gets the denomination of the coin.
     * @return The denomination.
     */
    public BigDecimal getDenomination() {
        return denomination;
    }
    /**
     * Sets the denomination of the coin.
     * @param denomination The denomination to set.
     */
    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }
    /**
     * Gets the quantity of coins available.
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }
    /**
     * Sets the quantity of coins available.
     * @param quantity The quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}