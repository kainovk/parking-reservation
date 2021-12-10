package ru.tinkoff.fintech.parking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.BookingRepository;
import ru.tinkoff.fintech.parking.model.Booking;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BookingService {

    private final BookingRepository repository;

    public void book(Booking booking) {
        repository.book(booking);
    }

    public void deleteBooking(UUID carId) {
        repository.deleteBooking(carId);
    }

    public List<Booking> findAll() {
        return repository.findAll();
    }
}
