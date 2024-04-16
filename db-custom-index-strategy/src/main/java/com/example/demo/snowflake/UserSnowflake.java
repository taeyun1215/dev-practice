package com.example.demo.snowflake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSnowflake {

    @Id
    private Long id;

    private String name;
    private String email;

}