package com.example.demo.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void createOrder(OrderCreateDTO orderCreateDTO) {
        Order order = Order.builder()
                .userId(orderCreateDTO.getUserId())
                .product(orderCreateDTO.getProduct())
                .quantity(orderCreateDTO.getQuantity())
                .build();

        orderRepository.save(order);
        redisTemplate.opsForValue().set("ORDER_" + order.getId(), order);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        Order cachedOrder = (Order) redisTemplate.opsForValue().get("ORDER_" + id);
        if (cachedOrder != null) {
            return Optional.of(cachedOrder);
        } else {
            Optional<Order> orderFromDb = orderRepository.findById(id);
            orderFromDb.ifPresent(order -> redisTemplate.opsForValue().set("ORDER_" + id, order));
            return orderFromDb;
        }
    }
}