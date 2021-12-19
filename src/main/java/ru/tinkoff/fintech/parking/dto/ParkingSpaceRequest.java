package ru.tinkoff.fintech.parking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingSpaceRequest {

    @NotNull
    @Range(min = 0)
    Integer x;
    @NotNull
    @Range(min = 0)
    Integer y;
    @NotNull
    Boolean busy;
}
