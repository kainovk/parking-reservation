package ru.tinkoff.fintech.parking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingSpace {

    UUID id;
    Integer x;
    Integer y;
    Boolean busy;

    private static class Mapper implements RowMapper<ParkingSpace> {

        @Override
        public ParkingSpace mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ParkingSpace(
                    UUID.fromString(rs.getString("id")),
                    rs.getInt("x"),
                    rs.getInt("y"),
                    rs.getBoolean("busy")
            );
        }
    }
}


