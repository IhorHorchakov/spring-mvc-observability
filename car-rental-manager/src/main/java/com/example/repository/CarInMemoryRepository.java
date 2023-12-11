package com.example.repository;

import com.example.metric.GaugeRegistry;
import com.example.repository.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class CarInMemoryRepository implements CarRepository {
    private Map<String, Car> storage = new HashMap<>();

    @Autowired
    public CarInMemoryRepository(GaugeRegistry gaugeRegistry) {
        gaugeRegistry.gaugeMap("Gauge:CarInMemoryRepository.storage.size", storage);
    }

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

    @Override
    public boolean isCarExists(String carId) {
        return this.storage.get(carId) != null;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

}
