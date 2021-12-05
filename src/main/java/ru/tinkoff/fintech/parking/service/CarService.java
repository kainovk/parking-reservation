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

    public void save(Car car) {
        repository.save(car);
    }

    public Car findById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Car> findAll() {
        return repository.findAll();
    }

    public void update(Car car) {
        repository.update(car);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }
}
