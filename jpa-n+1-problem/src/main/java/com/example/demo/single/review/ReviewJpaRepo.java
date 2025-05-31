package com.example.demo.single.review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepo extends JpaRepository<ReviewJpaEntity, Long> {

}