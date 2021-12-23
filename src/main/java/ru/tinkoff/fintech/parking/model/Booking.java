package ru.tinkoff.fintech.parking.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Booking {

    UUID carId;
    UUID psId;

    String timeFrom;
    String timeTo;

    private static class Mapper implements RowMapper<Booking> {

        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Booking(
                    UUID.fromString(rs.getString("carId")),
                    UUID.fromString(rs.getString("psId")),
                    rs.getString("timeFrom"),
                    rs.getString("timeTo")
            );
        }
    }
}
