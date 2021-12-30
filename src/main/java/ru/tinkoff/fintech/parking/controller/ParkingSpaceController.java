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
import ru.tinkoff.fintech.parking.dto.ParkingSpaceRequest;
import ru.tinkoff.fintech.parking.exception.ApplicationError;
import ru.tinkoff.fintech.parking.model.ParkingSpace;
import ru.tinkoff.fintech.parking.service.ParkingSpaceService;

import java.util.List;
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.ApplicationException;

@RestController
@RequestMapping("/parking-spaces")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @PostMapping("/create")
    public void createParkingSpace(@RequestBody @Validated ParkingSpaceRequest request) {
        ParkingSpace ps = ParkingSpace.builder()
                .id(UUID.randomUUID())
                .x(request.getX())
                .y(request.getY())
                .busy(request.getBusy())
                .build();

        parkingSpaceService.save(ps);
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

        parkingSpaceService.update(ps);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteParkingSpace(@PathVariable UUID id) {
        parkingSpaceService.delete(id);
    }

    @ExceptionHandler(ApplicationError.ApplicationException.class)
    public ResponseEntity<ApplicationException.ApplicationExceptionCompanion> handleApplicationException(ApplicationError.ApplicationException e) {
        return ResponseEntity.status(e.companion.code()).body(e.companion);
    }
}
