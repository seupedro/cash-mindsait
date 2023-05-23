package com.example.cashflow.dto.response;

import com.example.cashflow.domain.TransactionCategory;
import com.example.cashflow.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private UUID id;
    private BigDecimal value;
    private TransactionCategory category;
    private TransactionType type;
    private LocalDate transactionDate;

}
