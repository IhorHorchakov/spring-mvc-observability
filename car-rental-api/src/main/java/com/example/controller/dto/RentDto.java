package com.example.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RentDto {
    private String id;
    private String carId;
    private LocalDateTime from;
    private LocalDateTime to;
}
