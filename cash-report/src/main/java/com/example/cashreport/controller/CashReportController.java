package com.example.cashreport.controller;

import com.example.cashreport.dto.request.DailyReportRequest;
import com.example.cashreport.dto.response.BasicResponse;
import com.example.cashreport.dto.response.DailyReportResponse;
import com.example.cashreport.service.CashReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/api/transactions")
public class CashReportController {

    private final CashReportService reportService;

    public CashReportController(CashReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<DailyReportResponse>> getReportByDate(
            @RequestParam(name = "start_date") LocalDate startDate,
            @RequestParam(name = "end_date") LocalDate endDate ) {

        return ResponseEntity.ok(reportService.findReportByDateRange(startDate, endDate));
    }

    @PostMapping("/generate")
    public ResponseEntity<BasicResponse> generateReport(@RequestBody DailyReportRequest request) {
        BasicResponse response = BasicResponse.builder().status("skipped").build();

        if (request.executeTask()) {
            this.reportService.generate(request.getReportDate());
             response.setStatus("executed");
        }

        return ResponseEntity.ok().body(response);
    }
}
