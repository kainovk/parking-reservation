package ru.tinkoff.fintech.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintech.parking.dto.ParkingSpaceRequest;
import ru.tinkoff.fintech.parking.exception.ApplicationError;
import ru.tinkoff.fintech.parking.model.ParkingSpace;
import ru.tinkoff.fintech.parking.service.ParkingSpaceService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.*;

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

        Optional<ParkingSpace> psToFind = parkingSpaceService.findById(ps.getId());
        if (psToFind.isPresent()) {
            throw PARKING_SPACE_ALREADY_EXISTS.exception("Parking space " + ps.getId() + " is already exists");
        } else {
            parkingSpaceService.save(ps);
        }
    }

    @GetMapping("/get/{id}")
    public ParkingSpace getParkingSpace(@PathVariable UUID id) {
        return parkingSpaceService.findById(id).orElseThrow();
    }

    @GetMapping("/get")
    public List<ParkingSpace> getParkingSpaces() {
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

        Optional<ParkingSpace> psToFind = parkingSpaceService.findById(id);

        if (psToFind.isEmpty()) {
            throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + id + " not found");
        } else {
            parkingSpaceService.update(ps);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteParkingSpace(@PathVariable UUID id) {
        Optional<ParkingSpace> ps = parkingSpaceService.findById(id);

        if (ps.isEmpty()) {
            throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + id + " not found");
        }
        parkingSpaceService.delete(id);
    }

    @ExceptionHandler(ApplicationError.ApplicationException.class)
    public ResponseEntity<ApplicationException.ApplicationExceptionCompanion> handleApplicationException(ApplicationError.ApplicationException e) {
        return ResponseEntity.status(e.companion.code()).body(e.companion);
    }
}
