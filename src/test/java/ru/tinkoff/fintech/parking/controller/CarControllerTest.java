package ru.tinkoff.fintech.parking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.fintech.parking.AbstractTest;
import ru.tinkoff.fintech.parking.model.Car;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class CarControllerTest extends AbstractTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper jackson = new ObjectMapper();

    @Test
    void testCreateCarSuccess() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(post("/cars/create")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(carJson))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCarInvalidLength() throws Exception {
        Car car = prepareCarInvalidLength(UUID.randomUUID());
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(post("/cars/create")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(carJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testCreateCarInvalidWidth() throws Exception {
        Car car = prepareCarInvalidWidth(UUID.randomUUID());
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(post("/cars/create")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(carJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testCreateCarBlankModel() throws Exception {
        Car car = prepareCarBlankModel(UUID.randomUUID());
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(post("/cars/create")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(carJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testCreateCarBlankNumber() throws Exception {
        Car car = prepareCarBlankNumber(UUID.randomUUID());
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(post("/cars/create")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(carJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testGetCarSuccess() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDb(car);
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(get("/cars/get/")
                        .param("id", car.getId().toString())
                        .with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[" + carJson + "]"));
    }

    @Test
    void testUpdateCarSuccess() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDb(car);

        car.setModel("Porsche");
        car.setNumber("55NNN66");
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(put("/cars/update/" + car.getId())
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(carJson))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCarInvalidModel() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDb(car);

        car.setModel("");
        car.setNumber("55NNN66");
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(put("/cars/update/" + car.getId())
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(carJson))
                .andExpect(status().is(400));
    }

    @Test
    void testUpdateCarInvalidLength() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDb(car);

        car.setLength(-5);
        car.setNumber("55NNN66");
        var carJson = jackson.writeValueAsString(car);

        mockMvc.perform(put("/cars/update/" + car.getId())
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(carJson))
                .andExpect(status().is(400));
    }

    @Test
    void testDeleteCarSuccess() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDb(car);

        mockMvc.perform(delete("/cars/delete/" + car.getId())
                        .with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCarInvalidId() throws Exception {
        Car car = prepareCar(UUID.randomUUID());
        populateDb(car);

        mockMvc.perform(delete("/cars/delete/" + UUID.randomUUID())
                        .with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andDo(print())
                .andExpect(status().is(400));
    }

    private void populateDb(Car car) {
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

    private Car prepareCar(UUID id) {
        return Car.builder()
                .id(id)
                .model("Audi")
                .number("AB2345CD")
                .length(2)
                .width(1)
                .build();
    }

    private Car prepareCarInvalidLength(UUID id) {
        return Car.builder()
                .id(id)
                .model("Audi")
                .number("AB2345CD")
                .length(-1)
                .width(1)
                .build();
    }

    private Car prepareCarInvalidWidth(UUID id) {
        return Car.builder()
                .id(id)
                .model("Audi")
                .number("AB2345CD")
                .length(2)
                .width(0)
                .build();
    }

    private Car prepareCarBlankModel(UUID id) {
        return Car.builder()
                .id(id)
                .model("")
                .number("AB2345CD")
                .length(2)
                .width(1)
                .build();
    }

    private Car prepareCarBlankNumber(UUID id) {
        return Car.builder()
                .id(id)
                .model("Audi")
                .number("")
                .length(2)
                .width(1)
                .build();
    }
}
