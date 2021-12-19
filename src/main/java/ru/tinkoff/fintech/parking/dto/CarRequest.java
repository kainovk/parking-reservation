package ru.tinkoff.fintech.parking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarRequest {

    @NotBlank
    String model;
    @NotBlank
    String number;
    @NotNull
    @Range(min = 1)
    Integer length;
    @NotNull
    @Range(min = 1)
    Integer width;
}
