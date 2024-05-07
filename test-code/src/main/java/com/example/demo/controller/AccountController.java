package com.example.demo.controller;

import com.example.demo.controller.payload.AccountDto;
import com.example.demo.service.AccountService;
import com.example.demo.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public void createAccount(@RequestBody AccountDto accountDto) {
        accountService.save(accountDto);
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Account>> getAccountsByOwnerAndBalance(
            @RequestParam String owner,
            @RequestParam double balance
    ) {
        List<Account> accounts = accountService.findByOwnerAndBalanceGreaterThan(owner, balance);
        return ResponseEntity.ok(accounts);
    }
}
