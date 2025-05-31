package com.example.demo.integrationTest;

import com.example.demo.model.Account;
import com.example.demo.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTest extends IntegrationTestSupport {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUri() {
        return "http://localhost:" + port + "/api/accounts";
    }

    @Test
    void testCreateAccount() {
        // 계정 생성 요청
        Account account = Account.builder()
                .owner("John Doe")
                .balance(1000.0)
                .build();
        ResponseEntity<Account> response = restTemplate.postForEntity(getBaseUri(), account, Account.class);

        // 결과 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getOwner()).isEqualTo("John Doe");
        assertThat(response.getBody().getBalance()).isEqualTo(1000.0);
    }

    @Test
    void testGetAccountById() {
        // 계정 생성 및 ID 획득
        Account account = Account.builder()
                .owner("Jane Doe")
                .balance(500.0)
                .build();
        Account savedAccount = restTemplate.postForObject(getBaseUri(), account, Account.class);

        // 생성된 계정 ID로 조회
        ResponseEntity<Account> response = restTemplate.getForEntity(getBaseUri() + "/" + savedAccount.getId(), Account.class);

        // 결과 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedAccount.getId());
        assertThat(response.getBody().getOwner()).isEqualTo("Jane Doe");
    }
}