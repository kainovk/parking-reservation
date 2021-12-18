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
public class Car {

    UUID id;
    String model;
    String number;
    Integer length;
    Integer width;

    private static class Mapper implements RowMapper<Car> {

        @Override
        public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Car(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("model"),
                    rs.getString("number"),
                    rs.getInt("length"),
                    rs.getInt("width")
            );
        }
    }
}
