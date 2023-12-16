package com.example.client;

import com.example.controller.request.CarRentRequest;
import com.example.controller.response.CarRentResponse;
import com.example.controller.response.CarsResponse;
import io.micrometer.core.annotation.Timed;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CarRentalManagerClient {
    @Autowired
    @Qualifier("RentalManagerRestTemplate")
    private RestTemplate restTemplate;

    /*
       @Timed works only on public methods called by another class.
       @Timed method needs the so-called proxying which happens only between invocations of public methods.
     */
    @Timed("Timed:CarRentalManagerClient.getCars")
    @NewSpan("CarRentalManagerClient#getCars")
    public CarsResponse getCars() {
        return restTemplate.getForObject("/car-rent-manager/cars", CarsResponse.class);
    }

    /*
       @Timed works only on public methods called by another class.
       @Timed method needs the so-called proxying which happens only between invocations of public methods.
     */
    @Timed("Timed:CarRentalManagerClient.rentCar")
    @NewSpan("CarRentalManagerClient#rentCar")
    public CarRentResponse rentCar(@SpanTag CarRentRequest request) {
        return restTemplate.postForObject("/car-rent-manager/rent", request, CarRentResponse.class);
    }
}
