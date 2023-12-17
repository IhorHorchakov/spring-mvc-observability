package com.example.service;

import com.example.repository.entity.Car;
import com.example.repository.entity.Rent;
import com.example.repository.CarRepository;
import com.example.repository.RentalRepository;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.service.RentResult.RentStatus.ALREADY_IN_RENT;
import static com.example.service.RentResult.RentStatus.NOT_VALID;
import static com.example.service.RentResult.RentStatus.RENT_SUCCESSFULLY;

@Service
public class CarRentManager {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private RentalRepository rentalRepository;

    @PostConstruct
    public void createCars() {
        carRepository.save(Car.builder().make("Honda").model("Accord").year(2018).build());
        carRepository.save(Car.builder().make("Toyota").model("Camry").year(2020).build());
        carRepository.save(Car.builder().make("Lexus").model("RX").year(2017).build());
        carRepository.save(Car.builder().make("BMW").model("X6").year(2021).build());
        carRepository.save(Car.builder().make("Audi").model("Q7").year(2022).build());
    }

    public List<Car> getCars() {
        return carRepository.getCars();
    }

    @Observed(name = "Observed.CarRentManager.rentCar")
    public RentResult rentCar(String carId, LocalDateTime from, LocalDateTime to) {
        if (carId == null) {
            return new RentResult(NOT_VALID, "Rent is not valid, carId is null");
        }
        if (from.isAfter(to)) {
            return new RentResult(carId, NOT_VALID, "Rent is not valid, dateFrom stays after dateTo, carId = " + carId);
        }
        if (!carRepository.isCarExists(carId)) {
            return new RentResult(carId, NOT_VALID, "The car does not exist, carId = " + carId);
        }
        if (rentalRepository.isCarInRent(carId, from, to)) {
            return new RentResult(carId, ALREADY_IN_RENT, "The car is in rent already, pick another dates");
        }
        rentalRepository.save(Rent.builder().carId(carId).from(from).to(to).build());
        return new RentResult(carId, RENT_SUCCESSFULLY, "The car has been rent successfully");
    }
}
