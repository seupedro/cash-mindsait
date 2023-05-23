package com.example.cashreport.repository;

import com.example.cashreport.model.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, UUID> {

    Optional<DailyReport> findByReportDate(LocalDate localDate);
    List<DailyReport> findAllByReportDateBetween(LocalDate startDate, LocalDate endDate);
}