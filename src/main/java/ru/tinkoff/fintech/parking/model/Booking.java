package ru.tinkoff.fintech.parking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Time;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    UUID carId;
    UUID psId;
    Time timeFrom;
    Time timeTo;
}
