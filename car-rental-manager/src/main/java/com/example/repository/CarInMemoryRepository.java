package com.example.repository;

import com.example.repository.entity.Car;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Observed
public class CarInMemoryRepository implements CarRepository {
    private Map<String, Car> storage = new HashMap<>();

    @Override
    public Car save(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Unable to save null car");
        }
        if (car.getId() == null) {
            car.setId(generateId());
        }
        this.storage.put(car.getId(), car);
        return car;
    }

    @Override
    public List<Car> getCars() {
        return this.storage.values().stream().toList();
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

}
