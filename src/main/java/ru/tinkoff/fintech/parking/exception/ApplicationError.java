package ru.tinkoff.fintech.parking.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum ApplicationError {

    CAR_ALREADY_EXISTS("Car already exists", 400),
    CAR_NOT_FOUND("Car not found", 400),
    CAR_ALREADY_BOOKED("Car is already booked", 400),
    PARKING_SPACE_ALREADY_EXISTS("Parking space already exists", 400),
    PARKING_SPACE_NOT_FOUND("Parking space not found", 400),
    PARKING_SPACE_IS_ALREADY_TAKEN("Parking space is already taken", 400),
    BOOKING_NOT_FOUND("Booking not found", 400);

    private final String message;
    private final int code;

    ApplicationError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ApplicationException exception(String args) {
        return new ApplicationException(this, args);
    }

    public static class ApplicationException extends RuntimeException {

        public final ApplicationExceptionCompanion companion;

        ApplicationException(ApplicationError error, String message) {
            super(error.message + " : " + message);
            this.companion = new ApplicationExceptionCompanion(error.code, error.message + ": " + message);
        }

        public static record ApplicationExceptionCompanion(@JsonIgnore int code, String message) {
        }
    }
}
