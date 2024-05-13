package com.example.demo;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@Tag("context")
@SpringBootTest
@ActiveProfiles("local") // local로 되어있어도 횐경변수는 따로 맞춰줄 수 있음.
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class ClientContextTest {

}
