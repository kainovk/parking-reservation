package ru.tinkoff.fintech.parking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.CarRepository;
import ru.tinkoff.fintech.parking.model.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.CAR_ALREADY_EXISTS;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.CAR_NOT_FOUND;

@AllArgsConstructor
@Service
public class CarService {

    private final CarRepository repository;

    public void save(Car car) {
        Optional<Car> carToFind = repository.findById(car.getId());

        if (carToFind.isPresent()) {
            throw CAR_ALREADY_EXISTS.exception("Car " + car.getId() + " is already exists");
        } else {
            repository.save(car);
        }
    }

    public Optional<Car> findById(UUID id) {
        Optional<Car> carToFind = repository.findById(id);

        if (carToFind.isEmpty()) {
            throw CAR_NOT_FOUND.exception("Car with id=" + id + " not found");
        } else {
            return repository.findById(id);
        }
    }

    public List<Car> findAll() {
        return repository.findAll();
    }

    public void update(Car car) {
        Optional<Car> carToFind = repository.findById(car.getId());

        if (carToFind.isEmpty()) {
            throw CAR_NOT_FOUND.exception("Car " + car.getId() + " not found");
        } else {
            repository.update(car);
        }
    }

    public void delete(UUID id) {
        Optional<Car> car = repository.findById(id);

        if (car.isEmpty()) {
            throw CAR_NOT_FOUND.exception("Car " + id + " not found");
        } else {
            repository.delete(id);
        }
    }
}
