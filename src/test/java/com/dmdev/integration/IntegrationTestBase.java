package com.dmdev.integration;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.util.ConnectionManager;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;

public abstract class IntegrationTestBase {

    public static final User USER_WITH_FIRST_ID = User.builder()
            .id(1)
            .name("Ivan")
            .birthday(LocalDate.of(1990, 1, 10))
            .email("ivan@gmail.com")
            .password("111")
            .role(Role.ADMIN)
            .gender(Gender.MALE)
            .build();

    private static final String CLEAN_SQL = "DROP TABLE IF EXISTS users;";
    private static final String CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS users(id INT AUTO_INCREMENT PRIMARY KEY , name VARCHAR(64),birthday DATE NOT NULL ,email VARCHAR(64) NOT NULL UNIQUE ,password VARCHAR(64) NOT NULL ,role VARCHAR(32)  ,gender VARCHAR(16))";
    private static final String INSERT_SQL =
            "INSERT INTO users (name, birthday, email, password, role, gender) VALUES ('Ivan', '1990-01-10', 'ivan@gmail.com', '111', 'ADMIN', 'MALE'),     ('Petr', '1995-10-19', 'petr@gmail.com', '123', 'USER', 'MALE'),('Sveta', '2001-12-23', 'sveta@gmail.com', '321', 'USER', 'FEMALE'),('Vlad', '1984-03-14', 'vlad@gmail.com', '456', 'USER', 'MALE'),('Kate', '1989-08-09', 'kate@gmail.com', '777', 'ADMIN', 'FEMALE');";

    @BeforeEach
    @SneakyThrows
    void prepareDatabase() {
        try (Connection connection = ConnectionManager.get();
             Statement statement = connection.createStatement()) {
            statement.execute(CLEAN_SQL);
            statement.execute(CREATE_SQL);
            statement.execute(INSERT_SQL);
        }
    }
}
