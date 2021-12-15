package ru.tinkoff.fintech.parking.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.tinkoff.fintech.parking.model.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface BookingRepository {

    void book(Booking booking);

    Optional<Booking> findByCarId(UUID id);

    List<Booking> findAll();

    void update(Booking booking);

    void deleteBooking(UUID carId);
}
