package ru.tinkoff.fintech.parking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.ParkingSpaceRepository;
import ru.tinkoff.fintech.parking.model.ParkingSpace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository repository;

    public void save(ParkingSpace ps) {
        repository.save(ps);
    }

    public Optional<ParkingSpace> findById(UUID id) {
        return repository.findById(id);
    }

    public List<ParkingSpace> findAll() {
        return repository.findAll();
    }

    public void update(ParkingSpace ps) {
        repository.update(ps);
    }

    public void delete(UUID id) {
        repository.delete(id);
    }
}
