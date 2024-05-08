package com.example.demo.repository;

import com.example.demo.IntegrationTestSupport;
import com.example.demo.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
class AccountRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testFindByOwnerAndBalanceGreaterThan() {
        // Setup data scenario
        Account account1 = new Account(null, "John Doe", 1000.00);
        Account account2 = new Account(null, "John Doe", 500.00);
        entityManager.persist(account1);
        entityManager.persist(account2);
        entityManager.flush();

        // Test query
        List<Account> accounts = accountRepository.findByOwnerAndBalanceGreaterThan("John Doe", 750);

        // Validate results
        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getBalance()).isEqualTo(1000.00);
    }
}