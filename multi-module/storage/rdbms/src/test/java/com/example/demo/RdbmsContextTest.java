package com.example.demo;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tag("context")
@Testcontainers
@SpringBootTest
// @ActiveProfiles("test") // application-test.yml일 경우엔 이렇게 사용
@TestPropertySource(properties = {"spring.config.location = classpath:rdbms-application-test.yml"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class RdbmsContextTest {

	// MariaDB 컨테이너 설정
	@Container
	static MariaDBContainer<?> MARIADB_CONTAINER = new MariaDBContainer<>("mariadb:10.11")
		 .withInitScript("data/init.sql");

}
