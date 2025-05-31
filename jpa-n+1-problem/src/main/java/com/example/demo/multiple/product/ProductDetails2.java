package com.example.demo.multiple.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails2 {
    private Long productId;
    private String productName;
    private Double productPrice;
    private Long categoryId; // Category의 ID만 포함
}
