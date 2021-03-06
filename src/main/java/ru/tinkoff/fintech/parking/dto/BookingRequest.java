package ru.tinkoff.fintech.parking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.tinkoff.fintech.parking.validation.BookingConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@BookingConstraint
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {

    @NotNull
    UUID carId;
    @NotNull
    UUID psId;
    @NotBlank
    String timeFrom;
    @NotBlank
    String timeTo;
}
