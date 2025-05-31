package com.example.demo.multiple.product;

import com.example.demo.multiple.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails1 {
    private Long id;
    private String name;
    private Double price;
    private Category category;
}

