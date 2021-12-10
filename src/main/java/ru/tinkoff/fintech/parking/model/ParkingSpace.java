package ru.tinkoff.fintech.parking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingSpace {

    UUID id;
    Integer x;
    Integer y;
    Boolean busy;
}


