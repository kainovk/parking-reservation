package ru.tinkoff.fintech.parking.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.tinkoff.fintech.parking.model.Booking;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BookingRepository {

    void park(Booking booking);

    void leave(UUID carId);

    List<Booking> findAll();
}
