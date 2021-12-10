package ru.tinkoff.fintech.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintech.parking.dto.ParkingSpaceRequest;
import ru.tinkoff.fintech.parking.model.ParkingSpace;
import ru.tinkoff.fintech.parking.service.ParkingSpaceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/parking-spaces")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @PostMapping("/create")
    public void createParkingSpace(@RequestBody @Validated ParkingSpaceRequest request) {
        ParkingSpace ps = ParkingSpace.builder()
                .x(request.getX())
                .y(request.getY())
                .busy(request.getBusy())
                .build();
        parkingSpaceService.save(ps);
    }

    @GetMapping("/get/{id}")
    public ParkingSpace getParkingSpace(@PathVariable UUID id){
        return parkingSpaceService.findById(id);
    }

    @GetMapping("/get")
    public List<ParkingSpace> getParkingSpaces(){
        return parkingSpaceService.findAll();
    }

    @PutMapping("/update/{id}")
    public void updateParkingSpace(@PathVariable UUID id, @RequestBody @Validated ParkingSpaceRequest request) {
        ParkingSpace ps = ParkingSpace.builder()
                .id(id)
                .x(request.getX())
                .y(request.getY())
                .busy(request.getBusy())
                .build();
        parkingSpaceService.update(ps);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteParkingSpace(@PathVariable UUID id){
        parkingSpaceService.delete(id);
    }
}
