package com.example.demo.order;

import lombok.*;

@Getter
@AllArgsConstructor
public class OrderCreateDTO {
    private Long userId;
    private String product;
    private int quantity;
}
