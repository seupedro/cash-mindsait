package com.example.cashreport.client;

import com.example.cashreport.dto.response.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(value = "cash-flow", url = "${feing.url}")
public interface CashFlowClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/api/transactions")
    List<TransactionResponse> getAllTransactionsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);
}
