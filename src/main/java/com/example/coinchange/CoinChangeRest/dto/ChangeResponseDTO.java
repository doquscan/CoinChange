package com.example.coinchange.CoinChangeRest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ChangeResponseDTO {

    private Map<String, Integer> change;

    public ChangeResponseDTO(Map<BigDecimal, Integer> change) {
        // Convert BigDecimal keys to String keys for JSON serialization
        this.change = change.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toPlainString(),
                        Map.Entry::getValue
                ));
    }
//
//    public Map<String, Integer> getChange() {
//        return change;
//    }
//
//    public void setChange(Map<String, Integer> change) {
//        this.change = change;
//    }

    @Override
    public String toString() {
        return "ChangeResponseDTO{" +
                "change=" + change +
                '}';
    }


}
