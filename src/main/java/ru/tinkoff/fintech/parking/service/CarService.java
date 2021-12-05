package ru.tinkoff.fintech.parking.service;

import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.CarRepository;
import ru.tinkoff.fintech.parking.model.Car;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CarService {

    private final CarRepository repository;

    void save(Car car) {
        repository.save(car);
    }

    Car findById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    List<Car> findAll(@Param("ids") List<UUID> ids) {
        return repository.findAll(ids);
    }

    void update(Car car) {
        repository.update(car);
    }

    void delete(UUID id) {
        repository.delete(id);
    }

    void takeParkingSpace(UUID psId) {
        repository.takeParkingSpace(psId);
    }
}
