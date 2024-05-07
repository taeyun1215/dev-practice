package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.controller.payload.AccountDto;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Account 저장 서비스 테스트")
    void saveAccount() {
        // Given
        AccountDto accountDto = new AccountDto("John Doe", 1000.00);
        Account account = Account.builder()
                .owner(accountDto.getOwner())
                .balance(accountDto.getBalance())
                .build();

        // When
        accountService.save(accountDto);

        // Then
        verify(accountRepository).save(account);
    }

    @Test
    @DisplayName("Account Id 조회 서비스 테스트")
    void findAccountById() {
        // Given
        Long id = 1L;
        Account account = new Account(1L, "John Doe", 1000.00);
        when(accountRepository.findById(id)).thenReturn(java.util.Optional.of(account));

        // When
        Account foundAccount = accountService.findById(id);

        // Then
        assertThat(foundAccount).isEqualTo(account);
    }

    @Test
    @DisplayName("Account Id 조회 서비스 테스트 실패 EntityNotFoundException 발생")
    void findAccountById_NotFound1() {
        // Given
        Long id = 1L;
        when(accountRepository.findById(id)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.findById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Account with ID " + id + " not found");
    }

    @Test
    @DisplayName("Account Id 조회 서비스 테스트 실패 EntityNotFoundException 발생")
    void findAccountById_NotFound2() {
        // Given
        Long id = 1L;
        when(accountRepository.findById(id)).thenReturn(java.util.Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.findById(id);
        });
        assertEquals("Account with ID " + id + " not found", exception.getMessage());
    }

    @Test
    @DisplayName("Account Id 조회 서비스 테스트 실패 EntityNotFoundException 발생")
    void findAccountById_NotFound3() {
        // Given
        Long id = 1L;
        when(accountRepository.findById(id)).thenThrow(new EntityNotFoundException("Account with ID " + id + " not found"));

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> accountService.findById(id));
    }

    @Test
    @DisplayName("소유자와 잔액으로 계정 조회 서비스 테스트")
    void findAccountsByOwnerAndBalanceGreaterThan() {
        // Given
        String owner = "John Doe";
        double balance = 500.00;
        List<Account> expectedAccounts = List.of(
                new Account(1L, "John Doe", 1000.00)
        );
        when(accountRepository.findByOwnerAndBalanceGreaterThan(owner, balance)).thenReturn(expectedAccounts);

        // When
        List<Account> accounts = accountService.findByOwnerAndBalanceGreaterThan(owner, balance);

        // Then
        assertThat(accounts).isEqualTo(expectedAccounts);
        verify(accountRepository).findByOwnerAndBalanceGreaterThan(owner, balance);
    }
}

