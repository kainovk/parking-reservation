package ru.tinkoff.fintech.parking.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.tinkoff.fintech.parking.model.ParkingSpace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface ParkingSpaceRepository {

    void save(ParkingSpace ps);

    Optional<ParkingSpace> findById(UUID id);

    List<ParkingSpace> findAll();

    void update(ParkingSpace ps);

    void delete(UUID id);
}
