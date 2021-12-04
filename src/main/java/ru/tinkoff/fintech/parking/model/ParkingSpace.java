package ru.tinkoff.fintech.parking.model;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ParkingSpace extends Coordinates {

    @Setter
    private boolean busy;

    public boolean isBusy() {
        return busy;
    }
}
