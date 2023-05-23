package com.example.cashreport.dto.response;

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
public class DailyReportResponse {

    private UUID id;
    private BigDecimal totalBalance;
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
    private Long countOfDebits;
    private Long countOfCredits;
    private LocalDate reportDate;

}
