package com.example.repository.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Rent {
    private String id;
    private String carId;
    private LocalDateTime from;
    private LocalDateTime to;
}
