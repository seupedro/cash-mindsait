package com.example.cashreport.service;

import com.example.cashreport.client.CashFlowClient;
import com.example.cashreport.domain.TransactionCategory;
import com.example.cashreport.domain.TransactionType;
import com.example.cashreport.dto.response.DailyReportResponse;
import com.example.cashreport.dto.response.TransactionResponse;
import com.example.cashreport.model.DailyReport;
import com.example.cashreport.repository.DailyReportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashReportService {

    private final ModelMapper mapper;
    private final CashFlowClient cashFlowClient;
    private final DailyReportRepository dailyReportRepository;

    public CashReportService(ModelMapper mapper, CashFlowClient cashFlowClient, DailyReportRepository dailyReportRepository) {
        this.mapper = mapper;
        this.cashFlowClient = cashFlowClient;
        this.dailyReportRepository = dailyReportRepository;
    }

    public List<DailyReportResponse> findReportByDateRange(LocalDate startDate, LocalDate endDate) {
        return dailyReportRepository.findAllByReportDateBetween(startDate, endDate)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private DailyReportResponse toResponse(DailyReport dailyReport) {
        return mapper.map(dailyReport, DailyReportResponse.class);
    }

    @Transactional
    public void generate(LocalDate date) {
        List<TransactionResponse> transactions = cashFlowClient.getAllTransactionsByDate(date);
        DailyReport report = getFlushedReport(date);

        transactions.forEach(t -> {
            if (TransactionType.DEBIT.equals(t.getType())) {
                report.setTotalDebit(report.getTotalDebit().add(t.getValue()));
                report.setCountOfDebits(report.getCountOfDebits() + 1L);
            }

            if (TransactionType.CREDIT.equals(t.getType())) {
                report.setTotalCredit(report.getTotalCredit().add(t.getValue()));
                report.setCountOfCredits(report.getCountOfCredits() + 1L);
            }

            if (TransactionCategory.OPERATIONAL.equals(t.getCategory())) {
                report.setCountOfOperations(report.getCountOfOperations() + 1L);
            }

            if (TransactionCategory.INVESTMENT.equals(t.getCategory())) {
                report.setCountOfInvesting(report.getCountOfInvesting() + 1L);
            }

            if (TransactionCategory.FINANCING.equals(t.getCategory())) {
                report.setCountOfFinancing(report.getCountOfFinancing() + 1L);
            }
        });

        report.setTotalBalance(report.getTotalCredit().subtract(report.getTotalDebit()));
        dailyReportRepository.save(report);
    }

    private DailyReport getFlushedReport(LocalDate date) {
        DailyReport report = dailyReportRepository
                .findByReportDate(date)
                .orElse(DailyReport.builder().build());

        report.setTotalCredit(BigDecimal.ZERO);
        report.setTotalDebit(BigDecimal.ZERO);
        report.setTotalBalance(BigDecimal.ZERO);
        report.setCountOfCredits(0L);
        report.setCountOfDebits(0L);
        report.setCountOfFinancing(0L);
        report.setCountOfOperations(0L);
        report.setCountOfInvesting(0L);
        report.setReportDate(date);
        return report;
    }

}
