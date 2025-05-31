package com.example.demo;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

@Tag("context")
@SpringBootTest
//@ActiveProfiles("test") // application-test.yml일 경우엔 이렇게 사용
@TestPropertySource(properties = {"spring.config.location = classpath:client-application-test.yml"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class ClientContextTest {

}