package com.example.coinchange.CoinChangeRest.repository;

import com.example.coinchange.CoinChangeRest.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
