package com.example.demo.multiple.review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review, Long> {
}
