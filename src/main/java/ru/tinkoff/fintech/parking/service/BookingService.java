package ru.tinkoff.fintech.parking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.BookingRepository;
import ru.tinkoff.fintech.parking.model.Booking;
import ru.tinkoff.fintech.parking.model.ParkingSpace;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.BOOKING_NOT_FOUND;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.PARKING_SPACE_NOT_FOUND;

@EnableScheduling
@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParkingSpaceService psService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime dateTimeTo;

    @Scheduled(cron = "${cron.fixed_time_scanner_cron}")
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

    @Scheduled(cron = "${cron.midnight_scanner_cron}")
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

        Optional<Booking> booking = findByCarId(carId);

        if (booking.isEmpty()) {
            throw BOOKING_NOT_FOUND.exception("Booking with car id=" + carId + " not found");
        } else {
            Optional<ParkingSpace> ps = psService.findById(booking.get().getPsId());

            if(ps.isEmpty()){
                throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + booking.get().getPsId() + " not found");
            }else{
                ps.get().setBusy(false);
                psService.update(ps.get());
                bookingRepository.deleteBooking(carId);
            }
        }
    }
}
