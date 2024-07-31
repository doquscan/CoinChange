package com.example.coinchange.CoinChangeRest.repository;


import com.example.coinchange.CoinChangeRest.entity.CoinEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
/**
 * Repository interface for accessing CoinEntity data.
 */
@Repository
public interface CoinRepository extends JpaRepository<CoinEntity, BigDecimal> {

   /**
    * Finds all CoinEntities ordered by denomination in descending order.
    *
    * @return A list of CoinEntities ordered by denomination.
    */
   List<CoinEntity> findAllByOrderByDenominationDesc();

   //prevents other transactions from reading or writing the locked data until the current transaction completes
   /**
    * saves the updated quantity
    *
    * @return A list of CoinEntities ordered by denomination.
    */
   @Lock(LockModeType.PESSIMISTIC_WRITE)
   CoinEntity save(CoinEntity coin);

}
