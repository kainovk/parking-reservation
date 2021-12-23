package ru.tinkoff.fintech.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.fintech.parking.dto.CarRequest;
import ru.tinkoff.fintech.parking.exception.ApplicationError;
import ru.tinkoff.fintech.parking.model.Car;
import ru.tinkoff.fintech.parking.service.CarService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.ApplicationException;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.CAR_ALREADY_EXISTS;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.CAR_NOT_FOUND;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping("/create")
    public void createCar(@RequestBody @Validated CarRequest request) {
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .model(request.getModel())
                .number(request.getNumber())
                .length(request.getLength())
                .width(request.getWidth())
                .build();

        Optional<Car> carToFind = carService.findById(car.getId());
        if (carToFind.isPresent()) {
            throw CAR_ALREADY_EXISTS.exception("Car " + car.getId() + " is already exists");
        } else {
            carService.save(car);
        }
    }

    @GetMapping("/get/{id}")
    public Car getCar(@PathVariable UUID id) {
        Optional<Car> carToFind = carService.findById(id);
        if (carToFind.isEmpty()) {
            throw CAR_NOT_FOUND.exception("Car with id=" + id + " not found");
        } else {
            return carService.findById(id).orElseThrow();
        }
    }

    @GetMapping("/get")
    public List<Car> getCars() {
        return carService.findAll();
    }

    @PutMapping("/update/{id}")
    public void updateCar(@PathVariable UUID id, @RequestBody @Validated CarRequest request) {
        Car car = Car.builder()
                .id(id)
                .model(request.getModel())
                .number(request.getNumber())
                .length(request.getLength())
                .width(request.getWidth())
                .build();

        Optional<Car> carToFind = carService.findById(id);

        if (carToFind.isEmpty()) {
            throw CAR_NOT_FOUND.exception("Car " + id + " not found");
        } else {
            carService.update(car);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCar(@PathVariable UUID id) {
        Optional<Car> car = carService.findById(id);

        if (car.isEmpty()) {
            throw CAR_NOT_FOUND.exception("Car " + id + " not found");
        } else {
            carService.delete(id);
        }
    }

    @ExceptionHandler(ApplicationError.ApplicationException.class)
    public ResponseEntity<ApplicationException.ApplicationExceptionCompanion> handleApplicationException(ApplicationError.ApplicationException e) {
        return ResponseEntity.status(e.companion.code()).body(e.companion);
    }
}
