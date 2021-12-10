package ru.tinkoff.fintech.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintech.parking.dto.CarRequest;
import ru.tinkoff.fintech.parking.model.Car;
import ru.tinkoff.fintech.parking.service.CarService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping("/create")
    public void createCar(@RequestBody @Validated CarRequest request) {
        Car car = Car.builder()
                .model(request.getModel())
                .number(request.getNumber())
                .length(request.getLength())
                .width(request.getWidth())
                .build();
        carService.save(car);
    }

    @GetMapping("/get/{id}")
    public Car getCar(@PathVariable UUID id) {
        return carService.findById(id);
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
    public void deleteCar(@PathVariable UUID id){
        carService.delete(id);
    }
}
