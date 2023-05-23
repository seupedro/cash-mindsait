package com.example.cashflow.controller;


import com.example.cashflow.dto.request.TransactionRequest;
import com.example.cashflow.dto.response.TransactionResponse;
import com.example.cashflow.exception.ResourceNotFoundException;
import com.example.cashflow.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private static LocalDate localDateeeee;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactionsByDate(
            @RequestParam(name = "date", required = false) LocalDate nullableDate) {
        LocalDate date = Optional.ofNullable(nullableDate).orElse(LocalDate.now());
        return ResponseEntity.ok(transactionService.getAllTransactionsByDate(date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable UUID id, @RequestBody TransactionRequest request) throws ResourceNotFoundException {
        return ResponseEntity.ok(transactionService.updateTransaction(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) throws ResourceNotFoundException {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}

