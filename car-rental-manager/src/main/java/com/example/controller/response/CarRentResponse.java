package com.example.controller.response;

import com.example.service.RentResult;
import lombok.Data;

@Data
public class CarRentResponse {
    private String carId;
    private String rentStatus;
    private String detailedMessage;

    public static CarRentResponse from(RentResult result) {
        CarRentResponse response = new CarRentResponse();
        response.setCarId(result.getCarId());
        response.setRentStatus(result.getRentStatus().name());
        response.setDetailedMessage(result.getMessage());
        return response;
    }
}
