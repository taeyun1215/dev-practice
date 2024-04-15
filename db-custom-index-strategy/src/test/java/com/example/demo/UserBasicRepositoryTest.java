package com.example.demo;

import com.example.demo.basic.UserBasic;
import com.example.demo.basic.UserBasicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
public class UserBasicRepositoryTest {

    @Autowired
    private UserBasicRepository userBasicRepository;

    @Test
    public void testInsertThousandsOfUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 100000; i++) {
            userBasicRepository.save(
                    UserBasic.builder()
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