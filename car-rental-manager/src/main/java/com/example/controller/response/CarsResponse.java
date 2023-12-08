package com.example.controller.response;

import com.example.repository.entity.Car;
import lombok.Data;

import java.util.List;

@Data
public class CarsResponse {
    private List<Car> cars;
    public static CarsResponse from(List<Car> cars) {
        CarsResponse response = new CarsResponse();
        response.setCars(cars);
        return response;
    }
}
