package ru.tinkoff.fintech.parking.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.tinkoff.fintech.parking.model.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface CarRepository {

    void save(Car car);

    Optional<Car> findById(UUID id);

    List<Car> findAll();

    void update(Car car);

    void delete(UUID id);
}
