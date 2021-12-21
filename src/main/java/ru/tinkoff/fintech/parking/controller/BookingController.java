package ru.tinkoff.fintech.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintech.parking.dto.BookingRequest;
import ru.tinkoff.fintech.parking.exception.ApplicationError;
import ru.tinkoff.fintech.parking.model.Booking;
import ru.tinkoff.fintech.parking.model.Car;
import ru.tinkoff.fintech.parking.model.ParkingSpace;
import ru.tinkoff.fintech.parking.service.BookingService;
import ru.tinkoff.fintech.parking.service.CarService;
import ru.tinkoff.fintech.parking.service.ParkingSpaceService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final CarService carService;
    private final ParkingSpaceService psService;
    private final BookingService bookingService;

    @PostMapping("/book")
    public void book(@RequestBody @Validated @Valid BookingRequest request) {
        Booking booking = Booking.builder()
                .carId(request.getCarId())
                .psId(request.getPsId())
                .timeFrom(request.getTimeFrom())
                .timeTo(request.getTimeTo())
                .build();

        Optional<Booking> bookingToFind = bookingService.findByCarId(booking.getCarId());
        Optional<Car> car = carService.findById(booking.getCarId());
        Optional<ParkingSpace> ps = psService.findById(booking.getPsId());

        if (bookingToFind.isEmpty()) {
            if (ps.isEmpty()) {
                throw PARKING_SPACE_NOT_FOUND.exception(booking.getPsId().toString());
            } else if (car.isEmpty()) {
                throw CAR_NOT_FOUND.exception(booking.getCarId().toString());
            } else if (ps.get().getBusy()) {
                throw PARKING_SPACE_IS_ALREADY_TAKEN.exception("Parking space " + booking.getPsId().toString() + " is busy");
            } else {
                bookingService.book(booking);
                Optional<ParkingSpace> psToUpdate = psService.findById(booking.getPsId());
                psToUpdate.get().setBusy(true);
                psService.update(psToUpdate.get());
            }
        } else {
            throw CAR_ALREADY_BOOKED.exception(booking.getCarId().toString());
        }
    }

    @GetMapping("/get")
    public List<Booking> getBookings() {
        return bookingService.findAll();
    }

    @PutMapping("/update")
    public void updateBooking(@RequestBody @Validated @Valid BookingRequest request) {
        Booking booking = Booking.builder()
                .carId(request.getCarId())
                .psId(request.getPsId())
                .timeFrom(request.getTimeFrom())
                .timeTo(request.getTimeTo())
                .build();

        Optional<Car> car = carService.findById(booking.getCarId());
        Optional<ParkingSpace> newParkingSpace = psService.findById(booking.getPsId());

        Optional<Booking> oldBooking = bookingService.findByCarId(booking.getCarId());
        if (oldBooking.isEmpty()) {
            throw BOOKING_NOT_FOUND.exception("Booking with car id=" + booking.getCarId() + " not found");
        }
        Optional<ParkingSpace> oldParkingSpace = psService.findById(oldBooking.get().getPsId());

        if (car.isEmpty()) {
            throw CAR_NOT_FOUND.exception(booking.getCarId().toString());
        } else if (newParkingSpace.isEmpty()) {
            throw PARKING_SPACE_NOT_FOUND.exception(booking.getPsId().toString());
        } else if (newParkingSpace.get().getBusy() && !newParkingSpace.equals(oldParkingSpace)) {
            throw PARKING_SPACE_IS_ALREADY_TAKEN.exception("Parking space " + booking.getPsId().toString() + " is busy");
        } else {
            if (oldParkingSpace.isEmpty()) {
                throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + oldBooking.get().getPsId() + " not found");
            }
            oldParkingSpace.get().setBusy(false);
            psService.update(oldParkingSpace.get());
            newParkingSpace.get().setBusy(true);
            psService.update(newParkingSpace.get());
            bookingService.update(booking);
        }
    }

    @DeleteMapping("/delete/{carId}")
    public void deleteBooking(@PathVariable UUID carId) {
        Optional<Booking> booking = bookingService.findByCarId(carId);

        if (booking.isEmpty()) {
            throw BOOKING_NOT_FOUND.exception("Booking with car id=" + carId + " not found");
        } else {
            Optional<ParkingSpace> ps = psService.findById(booking.get().getPsId());

            if(ps.isEmpty()){
                throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + booking.get().getPsId() + " not found");
            }else{
                ps.get().setBusy(false);
                psService.update(ps.get());
                bookingService.deleteBooking(carId);
            }
        }
    }

    @DeleteMapping("/delete")
    public void deleteAll() {
        bookingService.deleteAll();
    }

    @ExceptionHandler(ApplicationError.ApplicationException.class)
    public ResponseEntity<ApplicationError.ApplicationException.ApplicationExceptionCompanion> handleApplicationException(ApplicationError.ApplicationException e) {
        return ResponseEntity.status(e.companion.code()).body(e.companion);
    }
}
