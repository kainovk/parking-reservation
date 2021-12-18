package ru.tinkoff.fintech.parking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.fintech.parking.AbstractTest;
import ru.tinkoff.fintech.parking.model.ParkingSpace;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ParkingSpaceControllerTest extends AbstractTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper jackson = new ObjectMapper();

    @Test
    void testCreateParkingSpaceSuccess() throws Exception {
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        var psJson = jackson.writeValueAsString(ps);

        mockMvc.perform(post("/parking-spaces/create")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(psJson))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateParkingSpaceInvalidCoordinates() throws Exception {
        ParkingSpace ps = prepareParkingSpaceInvalidCoordinates(UUID.randomUUID());
        var psJson = jackson.writeValueAsString(ps);

        mockMvc.perform(post("/parking-spaces/create")
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(psJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testGetCarSuccess() throws Exception {
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDb(ps);
        var psJson = jackson.writeValueAsString(ps);

        mockMvc.perform(get("/parking-spaces/get/")
                        .param("id", ps.getId().toString())
                        .with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[" + psJson + "]"));
    }

    @Test
    void testUpdateParkingSpaceSuccess() throws Exception {
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDb(ps);

        ps.setX(205);
        ps.setY(65);
        var psJson = jackson.writeValueAsString(ps);

        mockMvc.perform(put("/parking-spaces/update/" + ps.getId())
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(psJson))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateParkingSpaceInvalidCoordinates() throws Exception {
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDb(ps);

        ps.setX(-5);
        ps.setY(-1);
        var psJson = jackson.writeValueAsString(ps);

        mockMvc.perform(put("/parking-spaces/update/" + ps.getId())
                        .with(user("admin").password("pass").roles("USER", "ADMIN"))
                        .contentType("application/json")
                        .content(psJson))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void testDeleteParkingSpaceSuccess() throws Exception {
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDb(ps);

        mockMvc.perform(delete("/parking-spaces/delete/" + ps.getId())
                        .with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteParkingSpaceInvalidId() throws Exception {
        ParkingSpace ps = prepareParkingSpace(UUID.randomUUID());
        populateDb(ps);

        mockMvc.perform(delete("/parking-spaces/delete/" + UUID.randomUUID())
                        .with(user("admin").password("pass").roles("USER", "ADMIN")))
                .andExpect(status().is(400));
    }

    private void populateDb(ParkingSpace parkingSpace) {
        jdbcTemplate.update("""
                            INSERT INTO parking_space (id, x, y, busy)
                            VALUES (?,?,?,?)
                """, ps -> {
            ps.setString(1, parkingSpace.getId().toString());
            ps.setInt(2, parkingSpace.getX());
            ps.setInt(3, parkingSpace.getY());
            ps.setBoolean(4, parkingSpace.getBusy());
        });
    }

    private ParkingSpace prepareParkingSpace(UUID id) {
        return ParkingSpace.builder()
                .id(id)
                .x(1)
                .y(1)
                .busy(false)
                .build();
    }

    private ParkingSpace prepareParkingSpaceInvalidCoordinates(UUID id) {
        return ParkingSpace.builder()
                .id(id)
                .x(0)
                .y(-1)
                .busy(false)
                .build();
    }
}
