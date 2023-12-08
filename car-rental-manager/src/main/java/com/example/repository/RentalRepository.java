package com.example.repository;

import com.example.repository.entity.Rent;

import java.time.LocalDateTime;

public interface RentalRepository {
    Rent save(Rent rent);

    boolean isCarInRent(String carId, LocalDateTime from, LocalDateTime to);
}
