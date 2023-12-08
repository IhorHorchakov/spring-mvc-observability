package com.example.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RentResult {
    private String carId;
    private RentStatus rentStatus;
    private String message;

    public RentResult(RentStatus rentStatus, String message) {
        this.rentStatus = rentStatus;
        this.message = message;
    }

    public enum RentStatus {
        RENT_SUCCESSFULLY, ALREADY_IN_RENT, NOT_VALID
    }
}
