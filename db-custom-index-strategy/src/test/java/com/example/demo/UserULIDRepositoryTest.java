package com.example.demo;

import com.example.demo.ulid.UserULID;
import com.example.demo.ulid.UserULIDRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
public class UserULIDRepositoryTest {

    @Autowired
    private UserULIDRepository userULIDRepository;

    @Test
    public void testInsertThousandsOfUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 100000; i++) {
            userULIDRepository.save(
                    UserULID.builder()
                            .email("email" + i)
                            .name("name" + i)
                            .build()
            );
        }

        stopWatch.stop();

        System.out.println("ULID Insert Time: " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());
    }
}
