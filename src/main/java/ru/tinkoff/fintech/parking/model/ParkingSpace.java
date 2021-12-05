package ru.tinkoff.fintech.parking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingSpace {

    UUID id;
    int x;
    int y;
    boolean busy;

    public ParkingSpace(UUID id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }
}
