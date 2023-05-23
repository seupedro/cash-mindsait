package com.example.cashflow.repository;

import com.example.cashflow.domain.TransactionCategory;
import com.example.cashflow.domain.TransactionType;
import com.example.cashflow.model.Transaction;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureEmbeddedDatabase
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository repository;

    @Test
    @DisplayName("Test Auto UUID Entity Generation")
    public void testAutoUuidGeneration() {
        Transaction transaction = Transaction.builder()
                .value(BigDecimal.TEN)
                .type(TransactionType.CREDIT)
                .category(TransactionCategory.OPERATIONAL)
                .transactionDate(LocalDate.now())
                .build();

        Transaction savedTransaction = repository.save(transaction);
        Assertions.assertNotNull(savedTransaction.getId());
        Assertions.assertEquals(savedTransaction.getId().getClass(), UUID.class);
    }

}