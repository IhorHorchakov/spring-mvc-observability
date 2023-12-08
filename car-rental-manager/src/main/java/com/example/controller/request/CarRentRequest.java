package com.example.controller.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarRentRequest {
    private String carId;
    private LocalDateTime from;
    private LocalDateTime to;
}
