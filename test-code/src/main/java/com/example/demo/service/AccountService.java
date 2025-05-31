package com.example.demo.service;

import com.example.demo.controller.payload.AccountDto;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void save(AccountDto accountDto) {
        Account account = Account.builder()
                .owner(accountDto.getOwner())
                .balance(accountDto.getBalance())
                .build();

        accountRepository.save(account);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Account with ID " + id + " not found")
        );
    }

    public List<Account> findByOwnerAndBalanceGreaterThan(String owner, double balance) {
        return accountRepository.findByOwnerAndBalanceGreaterThan(owner, balance);
    }
}

