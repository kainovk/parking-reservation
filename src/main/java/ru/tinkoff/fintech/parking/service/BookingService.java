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

    public void park(Booking booking) {
        repository.park(booking);
    }

    public void leave(UUID carId) {
        repository.leave(carId);
    }

    public List<Booking> findAll() {
        return repository.findAll();
    }
}
