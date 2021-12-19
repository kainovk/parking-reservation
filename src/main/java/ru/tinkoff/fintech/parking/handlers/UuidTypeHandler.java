package ru.tinkoff.fintech.parking.handlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UuidTypeHandler extends BaseTypeHandler<UUID> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UUID uuid, JdbcType jdbcType
    ) throws SQLException {
        ps.setString(i, uuid.toString());
    }

    @Override
    public UUID getNullableResult(ResultSet rs, String s) throws SQLException {
        return rs.getObject(s, UUID.class);
    }

    @Override
    public UUID getNullableResult(ResultSet rs, int i) throws SQLException {
        return rs.getObject(i, UUID.class);
    }

    @Override
    public UUID getNullableResult(CallableStatement cs, int i) throws SQLException {
        return cs.getObject(i, UUID.class);
    }
}
