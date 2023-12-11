package com.example.repository;

import com.example.repository.entity.Car;

import java.util.List;

public interface CarRepository {
    Car save(Car car);

    List<Car> getCars();

    boolean isCarExists(String carId);
}
