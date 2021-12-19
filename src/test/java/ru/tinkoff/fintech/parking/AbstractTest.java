package ru.tinkoff.fintech.parking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractTest.class)
public class AbstractTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	public Connection dbConnection;

	@Override
	public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

	}

	@BeforeEach
	public void setup() throws SQLException {
		this.dbConnection = DriverManager.getConnection("jdbc:h2:mem:testdbtests", "sa", "password");
	}

	@AfterEach
	public void tearDownAndClearCache() throws Exception
	{
		Statement statement = dbConnection.createStatement();
		String queue = """
                DELETE FROM car;
                DELETE FROM parking_space;
                DELETE FROM booking;
                """;
		statement.executeUpdate(queue);
		dbConnection.close();
	}
}
