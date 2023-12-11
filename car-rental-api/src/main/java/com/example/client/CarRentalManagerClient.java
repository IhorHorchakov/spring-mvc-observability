package com.example.client;

import com.example.controller.request.CarRentRequest;
import com.example.controller.response.CarRentResponse;
import com.example.controller.response.CarsResponse;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarRentalManagerClient {
    @Value("${rental.manager.base.url}")
    private String baseUrl;
    private final RestTemplate restTemplate;

    /*
       @Timed works only on public methods called by another class.
       @Timed needs the so-called proxying which happens only between invocations of public methods.
     */
    @Timed("Timed:CarRentalManagerClient.getCars")
    public CarsResponse getCars() {
        return restTemplate.getForObject(baseUrl + "/car-rent-manager/cars", CarsResponse.class);
    }

    /*
       @Timed works only on public methods called by another class.
       @Timed needs the so-called proxying which happens only between invocations of public methods.
     */
    @Timed("Timed:CarRentalManagerClient.rentCar")
    public CarRentResponse rentCar(CarRentRequest request) {
        return restTemplate.postForObject(baseUrl + "/car-rent-manager/rent", request, CarRentResponse.class);
    }
}
