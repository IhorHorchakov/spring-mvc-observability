package com.example.controller;

import com.example.client.CarRentalManagerClient;
import com.example.controller.request.CarRentRequest;
import com.example.controller.response.CarRentResponse;
import com.example.controller.response.CarsResponse;
import io.micrometer.core.annotation.Counted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/car-rent-api")
public class RestApiController {

    @Autowired
    private CarRentalManagerClient client;

    @GetMapping(value = "/cars")
    @Counted("Counted:RestApiController.getCars")
    public ResponseEntity<CarsResponse> getCars() {
        CarsResponse response = client.getCars();
        return ResponseEntity.ofNullable(response);
    }

    @PostMapping("/rent")
    @Counted("Counted:RestApiController.postRentCar")
    public ResponseEntity<CarRentResponse> postRentCar(@RequestBody CarRentRequest request) {
        CarRentResponse response = client.rentCar(request);
        return ResponseEntity.ofNullable(response);
    }
}
