package ru.tinkoff.fintech.parking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.BookingRepository;
import ru.tinkoff.fintech.parking.dao.CarRepository;
import ru.tinkoff.fintech.parking.dao.ParkingSpaceRepository;
import ru.tinkoff.fintech.parking.model.Booking;
import ru.tinkoff.fintech.parking.model.Car;
import ru.tinkoff.fintech.parking.model.ParkingSpace;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.BOOKING_NOT_FOUND;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.CAR_ALREADY_BOOKED;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.CAR_NOT_FOUND;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.PARKING_SPACE_IS_ALREADY_TAKEN;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.PARKING_SPACE_NOT_FOUND;

@EnableScheduling
@RequiredArgsConstructor
@Service
public class BookingService {

    private final CarRepository carRepository;
    private final ParkingSpaceRepository psRepository;
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
        Optional<Car> car = carRepository.findById(booking.getCarId());
        Optional<ParkingSpace> ps = psRepository.findById(booking.getPsId());
        Optional<Booking> bookingToFind = bookingRepository.findByCarId(booking.getCarId());

        if (bookingToFind.isEmpty()) {
            if (ps.isEmpty()) {
                throw PARKING_SPACE_NOT_FOUND.exception(booking.getPsId().toString());
            } else if (car.isEmpty()) {
                throw CAR_NOT_FOUND.exception(booking.getCarId().toString());
            } else if (ps.get().getBusy()) {
                throw PARKING_SPACE_IS_ALREADY_TAKEN.exception("Parking space " + booking.getPsId().toString() + " is busy");
            } else {
                bookingRepository.book(booking);
                Optional<ParkingSpace> psToUpdate = psRepository.findById(booking.getPsId());
                psToUpdate.get().setBusy(true);
                psRepository.update(psToUpdate.get());
            }
        } else {
            throw CAR_ALREADY_BOOKED.exception(booking.getCarId().toString());
        }
    }

    public Optional<Booking> findByCarId(UUID id) {
        return bookingRepository.findByCarId(id);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public void update(Booking booking) {
        Optional<Car> car = carRepository.findById(booking.getCarId());
        Optional<ParkingSpace> newParkingSpace = psRepository.findById(booking.getPsId());
        Optional<Booking> oldBooking = bookingRepository.findByCarId(booking.getCarId());

        if (oldBooking.isEmpty()) {
            throw BOOKING_NOT_FOUND.exception("Booking with car id=" + booking.getCarId() + " not found");
        }

        Optional<ParkingSpace> oldParkingSpace = psRepository.findById(oldBooking.get().getPsId());

        if (car.isEmpty()) {
            throw CAR_NOT_FOUND.exception(booking.getCarId().toString());
        } else if (newParkingSpace.isEmpty()) {
            throw PARKING_SPACE_NOT_FOUND.exception(booking.getPsId().toString());
        } else if (newParkingSpace.get().getBusy() && !newParkingSpace.equals(oldParkingSpace)) {
            throw PARKING_SPACE_IS_ALREADY_TAKEN.exception("Parking space " + booking.getPsId().toString() + " is busy");
        } else if (oldParkingSpace.isEmpty()) {
            throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + oldBooking.get().getPsId() + " not found");
        } else {
            oldParkingSpace.get().setBusy(false);
            psRepository.update(oldParkingSpace.get());
            newParkingSpace.get().setBusy(true);
            psRepository.update(newParkingSpace.get());
            bookingRepository.update(booking);
        }
    }

    public void deleteBooking(UUID carId) {
        Optional<Booking> booking = findByCarId(carId);

        if (booking.isEmpty()) {
            throw BOOKING_NOT_FOUND.exception("Booking with car id=" + carId + " not found");
        } else {
            Optional<ParkingSpace> ps = psService.findById(booking.get().getPsId());

            if (ps.isEmpty()) {
                throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + booking.get().getPsId() + " not found");
            } else {
                ps.get().setBusy(false);
                psRepository.update(ps.get());
                bookingRepository.deleteBooking(carId);
            }
        }
    }
}
