package com.example.demo.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountDto {

    private String owner;
    private double balance;

}

