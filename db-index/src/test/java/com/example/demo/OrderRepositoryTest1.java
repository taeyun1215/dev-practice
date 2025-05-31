package com.example.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootTest
class OrderRepositoryTest1 {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    static void setUp(@Autowired OrderRepository orderRepository) {
        Random random = new Random();
        // 주문 데이터 100만 건 생성 및 저장
        for (int i = 0; i < 1000000; i++) {
            Long customerId = (long) (random.nextInt(100) + 1); // 1~100까지 랜덤 값
            LocalDateTime orderDate = LocalDateTime.now().minusDays(random.nextInt(365 * 2)); // 지난 2년간 랜덤 날짜

            orderRepository.save(
                    Order.builder()
                            .customerId(customerId)
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
        orderRepository.findByCustomerIdOrderByCreateDateDesc(targetCustomerId);
        stopWatch.stop();

        System.out.println("Query time with index: " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());
    }
}
