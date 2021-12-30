package ru.tinkoff.fintech.parking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintech.parking.dao.ParkingSpaceRepository;
import ru.tinkoff.fintech.parking.model.ParkingSpace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.tinkoff.fintech.parking.exception.ApplicationError.PARKING_SPACE_ALREADY_EXISTS;
import static ru.tinkoff.fintech.parking.exception.ApplicationError.PARKING_SPACE_NOT_FOUND;

@AllArgsConstructor
@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository repository;

    public void save(ParkingSpace ps) {
        Optional<ParkingSpace> psToFindById = repository.findById(ps.getId());

        if (psToFindById.isPresent()) {
            throw PARKING_SPACE_ALREADY_EXISTS.exception("Parking space " + ps.getId() + " is already exists");
        } else {
            try {
                repository.save(ps);
            } catch (Exception e) {
                throw PARKING_SPACE_ALREADY_EXISTS.exception("Parking space with coordinates (" + ps.getX() + ", " + ps.getY() + ") is already exists");
            }
        }
    }

    public Optional<ParkingSpace> findById(UUID id) {
        Optional<ParkingSpace> parkingSpaceToFind = repository.findById(id);

        if (parkingSpaceToFind.isEmpty()) {
            throw PARKING_SPACE_NOT_FOUND.exception("Parking space with id=" + id + " not found");
        } else {
            return repository.findById(id);
        }
    }

    public List<ParkingSpace> findAll() {
        return repository.findAll();
    }

    public void update(ParkingSpace ps) {
        Optional<ParkingSpace> psToFind = repository.findById(ps.getId());

        if (psToFind.isEmpty()) {
            throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + ps.getId() + " not found");
        } else {
            repository.update(ps);
        }
    }

    public void delete(UUID id) {
        Optional<ParkingSpace> ps = repository.findById(id);

        if (ps.isEmpty()) {
            throw PARKING_SPACE_NOT_FOUND.exception("Parking space " + id + " not found");
        } else {
            repository.delete(id);
        }
    }
}
