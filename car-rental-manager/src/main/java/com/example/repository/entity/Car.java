package com.example.repository.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Car {
    private String id;
    private String make;
    private String model;
    private int year;
}
