package ru.tinkoff.fintech.parking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.fintech.parking.AbstractTest;
import ru.tinkoff.fintech.parking.model.Booking;
import ru.tinkoff.fintech.parking.model.Car;
import ru.tinkoff.fintech.parking.model.ParkingSpace;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class BookingControllerTest extends AbstractTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper jackson = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void testCreateBookingSuccess() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDbCar(car);
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDbParkingSpace(ps);
        Booking booking = prepareBooking(car.getId(), ps.getId());
        var bookingJson = jackson.writeValueAsString(booking);

        LocalDateTime now = LocalDateTime.now();
        if (now.getDayOfWeek() == DayOfWeek.SATURDAY || now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            mockMvc.perform(post("/bookings/book")
                            .with(user("admin").password("pass").roles("USER", "ADMIN"))
                            .contentType("application/json")
                            .content(bookingJson))
                    .andDo(print())
                    .andExpect(status().is(400));
        } else {
            mockMvc.perform(post("/bookings/book")
                            .with(user("admin").password("pass").roles("USER", "ADMIN"))
                            .contentType("application/json")
                            .content(bookingJson))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testCreateBookingInvalidTime() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDbCar(car);
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDbParkingSpace(ps);
        Booking booking = prepareBookingInvalidTime(car.getId(), ps.getId());
        var bookingJson = jackson.writeValueAsString(booking);

        mockMvc.perform(post("/bookings/book")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(bookingJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testCreateBookingInvalidCar() throws Exception {
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDbParkingSpace(ps);
        Booking booking = prepareBookingInvalidTime(UUID.randomUUID(), ps.getId());
        var bookingJson = jackson.writeValueAsString(booking);

        mockMvc.perform(post("/bookings/book")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(bookingJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testCreateBookingInvalidParkingSpace() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDbCar(car);
        Booking booking = prepareBookingInvalidTime(car.getId(), UUID.randomUUID());
        var bookingJson = jackson.writeValueAsString(booking);

        mockMvc.perform(post("/bookings/book")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(bookingJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testCreateBookingAlreadyExists() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDbCar(car);
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDbParkingSpace(ps);
        Booking booking = prepareBooking(car.getId(), ps.getId());
        populateDbBooking(booking);
        var bookingJson = jackson.writeValueAsString(booking);

        mockMvc.perform(post("/bookings/book")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(bookingJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testUpdateBookingSuccess() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDbCar(car);
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDbParkingSpace(ps);
        Booking booking = prepareBooking(car.getId(), ps.getId());
        populateDbBooking(booking);

        ParkingSpace newPs = prepareParkingSpace(UUID.randomUUID());
        newPs.setX(100);
        populateDbParkingSpace(newPs);
        booking.setPsId(newPs.getId());
        var bookingJson = jackson.writeValueAsString(booking);

        LocalDateTime now = LocalDateTime.now();
        if (now.getDayOfWeek() == DayOfWeek.SATURDAY || now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            mockMvc.perform(put("/bookings/update")
                            .with(user("admin").password("pass").roles("USER", "ADMIN"))
                            .contentType("application/json")
                            .content(bookingJson))
                    .andDo(print())
                    .andExpect(status().is(400));
        } else {
            mockMvc.perform(put("/bookings/update")
                            .with(user("admin").password("pass").roles("USER", "ADMIN"))
                            .contentType("application/json")
                            .content(bookingJson))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testUpdateBookingNotFound() throws Exception {
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDbParkingSpace(ps);
        Booking booking = prepareBooking(UUID.randomUUID(), ps.getId());
        var bookingJson = jackson.writeValueAsString(booking);

        mockMvc.perform(put("/bookings/update")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(bookingJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testUpdateBookingBusyParkingSpace() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDbCar(car);
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDbParkingSpace(ps);
        Booking booking = prepareBooking(car.getId(), ps.getId());
        populateDbBooking(booking);

        ParkingSpace newPs = prepareParkingSpace(UUID.randomUUID());
        newPs.setX(100);
        newPs.setBusy(true);
        populateDbParkingSpace(newPs);
        Booking newBooking = prepareBooking(car.getId(), newPs.getId());
        var bookingJson = jackson.writeValueAsString(newBooking);

        mockMvc.perform(put("/bookings/update")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(bookingJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testDeleteBookingSuccess() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDbCar(car);
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDbParkingSpace(ps);
        Booking booking = prepareBooking(car.getId(), ps.getId());
        populateDbBooking(booking);


        mockMvc.perform(delete("/bookings/delete/" + booking.getCarId())
                        .with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBookingInvalidId() throws Exception {
        mockMvc.perform(delete("/bookings/delete/" + UUID.randomUUID())
                        .with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andDo(print())
                .andExpect(status().is(400));
    }

    private void populateDbCar(Car car) {
        jdbcTemplate.update("""
                            INSERT INTO car (id, model, number, length, width)
                            VALUES (?,?,?,?,?)
                """, ps -> {
            ps.setString(1, car.getId().toString());
            ps.setString(2, car.getModel());
            ps.setString(3, car.getNumber());
            ps.setInt(4, car.getLength());
            ps.setInt(5, car.getWidth());
        });
    }

    private void populateDbParkingSpace(ParkingSpace parkingSapce) {
        jdbcTemplate.update("""
                            INSERT INTO parking_space (id, x, y, busy)
                            VALUES (?,?,?,?)
                """, ps -> {
            ps.setString(1, parkingSapce.getId().toString());
            ps.setInt(2, parkingSapce.getX());
            ps.setInt(3, parkingSapce.getY());
            ps.setBoolean(4, parkingSapce.getBusy());
        });
    }

    private void populateDbBooking(Booking booking) {
        jdbcTemplate.update("""
                            INSERT INTO booking (car_id, ps_id, time_from, time_to)
                            VALUES (?,?,?,?)
                """, ps -> {
            ps.setString(1, booking.getCarId().toString());
            ps.setString(2, booking.getPsId().toString());
            ps.setString(3, booking.getTimeFrom());
            ps.setString(4, booking.getTimeTo());
        });
    }

    private Car prepareCar(UUID id) {
        return Car.builder()
                .id(id)
                .model("Audi")
                .number("AB2345CD")
                .length(2)
                .width(1)
                .build();
    }

    private ParkingSpace prepareParkingSpace(UUID id) {
        return ParkingSpace.builder()
                .id(id)
                .x(1)
                .y(1)
                .busy(false)
                .build();
    }

    private Booking prepareBooking(UUID carId, UUID psId) {

        LocalDateTime nowPlus1Hour = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        LocalDateTime nowPlus5Hours = LocalDateTime.now().plus(5, ChronoUnit.HOURS);
        String nowPlus1HourStr = nowPlus1Hour.format(formatter);
        String nowPlus5HoursStr = nowPlus5Hours.format(formatter);

        return Booking.builder()
                .carId(carId)
                .psId(psId)
                .timeFrom(nowPlus1HourStr)
                .timeTo(nowPlus5HoursStr)
                .build();
    }

    private Booking prepareBookingInvalidTime(UUID carId, UUID psId) {

        LocalDateTime nowPlus1Hour = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        String nowPlus1HourStr = nowPlus1Hour.format(formatter);

        return Booking.builder()
                .carId(carId)
                .psId(psId)
                .timeFrom(nowPlus1HourStr)
                .timeTo(nowPlus1HourStr)
                .build();
    }

}
