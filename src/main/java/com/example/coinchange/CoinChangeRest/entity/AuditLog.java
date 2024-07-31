package com.example.coinchange.CoinChangeRest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * Entity representing an audit log entry.
 * This class records the details of each transaction involving coin change.
 */

//@Data
@Entity
@Table(name="audit_log")
public class AuditLog {

    /**
     * The unique identifier for the audit log entry.
     * Automatically generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The bill amount for which change was requested.
     */
    private int bill;
    /**
     * The timestamp of when the transaction was logged.
     */
    private String changeDetails;
    /**
     * Default constructor for JPA.
     */
    private LocalDateTime timestamp;

    // Constructors, getters, and setters

    public AuditLog() {}
    /**
     * Constructs a new AuditLog with the specified details.
     *
     * @param bill the bill amount for which change was requested.
     * @param changeDetails a string representation of the change provided.
     * @param timestamp the timestamp of when the transaction was logged.
     */
    public AuditLog(int bill, String changeDetails, LocalDateTime timestamp) {
        this.bill = bill;
        this.changeDetails = changeDetails;
        this.timestamp = timestamp;
    }
    /**
     * Gets the unique identifier of the audit log entry.
     *
     * @return the ID of the audit log entry.
     */
    public Long getId() {
        return id;
    }
    /**
     * Sets the unique identifier of the audit log entry.
     *
     * @param id the new ID of the audit log entry.
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Gets the bill amount for which change was requested.
     *
     * @return the bill amount.
     */
    public int getBill() {
        return bill;
    }
    /**
     * Sets the bill amount for which change was requested.
     *
     * @param bill the new bill amount.
     */
    public void setBill(int bill) {
        this.bill = bill;
    }
    /**
     * Gets the string representation of the change provided.
     *
     * @return the change details.
     */
    public String getChangeDetails() {
        return changeDetails;
    }
    /**
     * Sets the string representation of the change provided.
     *
     * @param changeDetails the new change details.
     */
    public void setChangeDetails(String changeDetails) {
        this.changeDetails = changeDetails;
    }
    /**
     * Gets the timestamp of when the transaction was logged.
     *
     * @return the timestamp.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    /**
     * Sets the timestamp of when the transaction was logged.
     *
     * @param timestamp the new timestamp.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
