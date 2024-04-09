package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 특정 고객의 최근 주문순 조회
    List<Order> findByCustomerIdOrderByCreateDateDesc(Long customerId);
}
