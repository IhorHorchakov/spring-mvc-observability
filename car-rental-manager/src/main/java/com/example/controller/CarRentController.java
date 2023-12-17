package com.example.controller;

import com.example.controller.request.CarRentRequest;
import com.example.controller.response.CarRentResponse;
import com.example.controller.response.CarsResponse;
import com.example.service.CarRentManager;
import com.example.service.RentResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/car-rent-manager")
@Slf4j
public class CarRentController {
    @Autowired
    private CarRentManager manager;

    @GetMapping("/cars")
    public ResponseEntity<CarsResponse> getCars() {
        CarsResponse response = CarsResponse.from(manager.getCars());
        return ResponseEntity.ofNullable(response);
    }

    @PostMapping("/rent")
    public ResponseEntity<CarRentResponse> postRentCar(@RequestBody CarRentRequest request) {
        log.info("Receive CarRentRequest: {}", request);
        RentResult result = manager.rentCar(request.getCarId(), request.getFrom(), request.getTo());
        CarRentResponse response = CarRentResponse.from(result);
        log.info("CarRentResponse: {}", response);
        return ResponseEntity.ofNullable(response);
    }
}
