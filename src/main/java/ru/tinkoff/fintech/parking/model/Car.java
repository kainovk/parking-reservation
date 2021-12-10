package ru.tinkoff.fintech.parking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Car {

    UUID id;
    String model;
    String number;
    Integer length;
    Integer width;
}
