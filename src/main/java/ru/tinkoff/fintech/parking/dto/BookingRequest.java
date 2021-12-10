package ru.tinkoff.fintech.parking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.UUID;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {

    @NotNull
    UUID carId;
    @NotNull
    UUID psId;
    @NotNull
    Time timeFrom;
    @NotNull
    Time timeTo;
}
