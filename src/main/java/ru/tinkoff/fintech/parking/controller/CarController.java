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
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.ApplicationException;

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

        carService.save(car);
    }

    @GetMapping("/get/{id}")
    public Car getCar(@PathVariable UUID id) {
        return carService.findById(id).orElseThrow();
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

        carService.update(car);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCar(@PathVariable UUID id) {
        carService.delete(id);
    }

    @ExceptionHandler(ApplicationError.ApplicationException.class)
    public ResponseEntity<ApplicationException.ApplicationExceptionCompanion> handleApplicationException(ApplicationError.ApplicationException e) {
        return ResponseEntity.status(e.companion.code()).body(e.companion);
    }
}
