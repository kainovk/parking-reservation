package ru.tinkoff.fintech.parking.validation;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.fintech.parking.dto.BookingRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class BookingValidator implements ConstraintValidator<BookingConstraint, BookingRequest> {

    @Override
    public boolean isValid(BookingRequest booking, ConstraintValidatorContext constraintValidatorContext) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTimeFrom = LocalDateTime.parse(booking.getTimeFrom(), formatter);
        LocalDateTime dateTimeTo = LocalDateTime.parse(booking.getTimeTo(), formatter);
        String nowStr = LocalDateTime.now().format(formatter);
        LocalDateTime now = LocalDateTime.parse(nowStr, formatter);
        long minutes = ChronoUnit.MINUTES.between(dateTimeFrom, dateTimeTo);

        return dateTimeFrom.isBefore(dateTimeTo) &&
               dateTimeFrom.isAfter(now) &&
               dateTimeFrom.getDayOfWeek() != DayOfWeek.SATURDAY &&
               dateTimeFrom.getDayOfWeek() != DayOfWeek.SUNDAY &&
               minutes >= 60 && minutes <= 60 * 24;
    }
}
