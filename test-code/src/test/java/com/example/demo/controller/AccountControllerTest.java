package com.example.demo.controller;

import com.example.demo.controller.payload.AccountDto;
import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("Account 저장 컨트롤러 테스트")
    public void createAccount_ShouldCallService() throws Exception {
        // Given
        AccountDto accountDto = new AccountDto("John Doe", 1000.00);

        // When
        doNothing().when(accountService).save(accountDto);

        // Then
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"owner\":\"John Doe\",\"balance\":1000.00}"))
                .andExpect(status().isOk());

        verify(accountService).save(any(AccountDto.class)); // 위 아래가 같은 내용임.
        verify(accountService, times(1)).save(any(AccountDto.class));
    }

    @Test
    @DisplayName("Account Id 조회 컨트롤러 테스트")
    public void getAccountById_ShouldReturnAccount() throws Exception {
        // Given
        Account mockAccount = new Account(1L, "John Doe", 1000.00);

        // When
        given(accountService.findById(1L)).willReturn(mockAccount);

        // Then
        mockMvc.perform(get("/api/accounts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.owner").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(1000.00));

        verify(accountService).findById(any(Long.class));
    }

    @Test
    @DisplayName("소유자와 잔액으로 계정 조회 컨트롤러 테스트")
    public void getAccountsByOwnerAndBalance_ShouldReturnAccounts() throws Exception {
        // Given
        List<Account> accounts = List.of(
                new Account(1L, "John Doe", 1500.00)
        );

        // When
        given(accountService.findByOwnerAndBalanceGreaterThan("John Doe", 1000.00)).willReturn(accounts);

        // Then
        mockMvc.perform(get("/api/accounts/search")
                        .param("owner", "John Doe")
                        .param("balance", "1000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].owner").value("John Doe"))
                .andExpect(jsonPath("$[0].balance").value(1500.00));

        verify(accountService).findByOwnerAndBalanceGreaterThan(any(String.class), any(Double.class));
    }
}
