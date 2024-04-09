package com.example.demo;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootTest
class OrderRepositoryTest2 {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    static void setUp(@Autowired OrderRepository orderRepository) {
        Random random = new Random();
        // 주문 데이터 10만 건 생성 및 저장
        for (int i = 0; i < 100000; i++) {
            LocalDateTime orderDate = LocalDateTime.now().minusDays(random.nextInt(365 * 2)); // 지난 2년간 랜덤 날짜

            orderRepository.save(
                    Order.builder()
                            .customerId((long) (i))
                            .createDate(orderDate)
                            .build()
            );
        }
    }

    @Test
    void performanceTestWithIndex() {
        StopWatch stopWatch = new StopWatch();

        // 특정 고객의 최근 주문 조회
        Long targetCustomerId = 100L;
        stopWatch.start();
        orderRepository.findByCustomerId(targetCustomerId);
        stopWatch.stop();

        System.out.println("Query time with index: " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());
    }
}
