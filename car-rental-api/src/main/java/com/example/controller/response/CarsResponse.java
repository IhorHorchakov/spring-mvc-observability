package com.example.controller.response;

import com.example.controller.dto.CarDto;
import lombok.Data;

import java.util.List;

@Data
public class CarsResponse {
    private List<CarDto> cars;

    public static CarsResponse from(List<CarDto> cars) {
        CarsResponse response = new CarsResponse();
        response.setCars(cars);
        return response;
    }
}
