package com.example.controller.response;

import lombok.Data;

@Data
public class CarRentResponse {
    private String carId;
    private String rentStatus;
    private String detailedMessage;
}
