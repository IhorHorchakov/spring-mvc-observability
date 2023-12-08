package com.example.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDto {
    private String id;
    private String make;
    private String model;
    private int year;
}
