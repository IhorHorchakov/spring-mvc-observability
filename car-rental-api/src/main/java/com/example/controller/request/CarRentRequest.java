package com.example.controller.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CarRentRequest {
    private String carId;
    private LocalDateTime from;
    private LocalDateTime to;
}
