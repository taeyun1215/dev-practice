package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // 필요에 따라 생성자 접근 수준 조정
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
