package ru.tinkoff.fintech.parking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.BookingRepository;
import ru.tinkoff.fintech.parking.model.Booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableScheduling
@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime dateTimeTo;

    @Scheduled(cron = "0/15 * * ? * MON-FRI")
    public void scanToDelete() {
        String nowStr = LocalDateTime.now().format(formatter);
        LocalDateTime now = LocalDateTime.parse(nowStr, formatter);

        List<Booking> bookings = findAll();
        bookings.forEach(it -> {
            dateTimeTo = LocalDateTime.parse(it.getTimeTo(), formatter);
            if (!now.isBefore(dateTimeTo)) {
                deleteBooking(it.getCarId());
            }
        });
    }

    @Scheduled(cron = "0 0 0 ? * MON-FRI")
    public void deleteAll() {
        List<Booking> bookings = findAll();
        bookings.forEach(it -> {
            deleteBooking(it.getCarId());
        });
    }

    public void book(Booking booking) {
        bookingRepository.book(booking);
    }

    public Optional<Booking> findByCarId(UUID id) {
        return bookingRepository.findByCarId(id);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public void update(Booking booking) {
        bookingRepository.update(booking);
    }

    public void deleteBooking(UUID carId) {
        bookingRepository.deleteBooking(carId);
    }
}
