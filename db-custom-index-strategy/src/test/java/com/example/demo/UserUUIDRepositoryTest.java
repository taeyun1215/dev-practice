package com.example.demo;

import com.example.demo.uuid.UserUUID;
import com.example.demo.uuid.UserUUIDRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
public class UserUUIDRepositoryTest {

    @Autowired
    private UserUUIDRepository userUUIDRepository;

    @Test
    public void testInsertThousandsOfUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 100000; i++) {
            userUUIDRepository.save(
                    UserUUID.builder()
                            .email("email" + i)
                            .name("name" + i)
                            .build()
            );
        }

        stopWatch.stop();

        System.out.println("Query time with index: " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());
    }
}