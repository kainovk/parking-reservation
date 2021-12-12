package ru.tinkoff.fintech.parking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintech.parking.dto.BookingRequest;
import ru.tinkoff.fintech.parking.model.Booking;
import ru.tinkoff.fintech.parking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/book")
    public void book(@RequestBody @Validated @Valid BookingRequest request) {
        Booking booking = Booking.builder()
                .carId(request.getCarId())
                .psId(request.getPsId())
                .timeFrom(request.getTimeFrom())
                .timeTo(request.getTimeTo())
                .build();
        bookingService.book(booking);
    }

    @GetMapping("/get")
    public List<Booking> getBookings() {
        return bookingService.findAll();
    }

    //TODO
    @PutMapping("/update/{carId}")
    public void updateBooking(@RequestBody @Validated @Valid BookingRequest request) {
        Booking booking = Booking.builder()
                .psId(request.getPsId())
                .timeFrom(request.getTimeFrom())
                .timeTo(request.getTimeTo())
                .build();
        //bookingService.update(booking);
    }

    @DeleteMapping("/delete/{carId}")
    public void deleteBooking(@PathVariable UUID carId) {
        bookingService.deleteBooking(carId);
    }
}
