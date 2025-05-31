package com.example.demo.multiple.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.reviews r JOIN FETCH r.customer")
    List<Product> findAllByMultiFetchJoin();

}
