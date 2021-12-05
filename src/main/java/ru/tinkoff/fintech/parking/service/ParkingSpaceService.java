package ru.tinkoff.fintech.parking.service;

import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.ParkingSpaceRepository;
import ru.tinkoff.fintech.parking.model.ParkingSpace;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository repository;

    void save(ParkingSpace ps){
        repository.save(ps);
    }

    ParkingSpace findById(UUID id){
        return repository.findById(id).orElseThrow();
    }

    List<ParkingSpace> findAll(@Param("ids") List<UUID> ids){
        return repository.findAll(ids);
    }

    void update(ParkingSpace ps){
        repository.update(ps);
    }

    void delete(UUID id){
        repository.delete(id);
    }
}
