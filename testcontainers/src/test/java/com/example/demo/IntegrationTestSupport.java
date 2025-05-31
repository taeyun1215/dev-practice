package com.example.demo;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationTestSupport {

    // MariaDB 컨테이너 설정
    @Container
    static MariaDBContainer<?> MARIADB_CONTAINER = new MariaDBContainer<>("mariadb:10.11")
        .withInitScript("data/init.sql");

}
