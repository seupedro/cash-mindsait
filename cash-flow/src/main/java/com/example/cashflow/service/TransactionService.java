package com.example.cashflow.service;

import com.example.cashflow.dto.request.TransactionRequest;
import com.example.cashflow.dto.response.TransactionResponse;
import com.example.cashflow.exception.ResourceNotFoundException;
import com.example.cashflow.model.Transaction;
import com.example.cashflow.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final ModelMapper mapper;

    public TransactionService(TransactionRepository transactionRepository, ModelMapper mapper) {
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }

    public List<TransactionResponse> getAllTransactionsByDate(LocalDate date) {
        return transactionRepository
                .findAllByTransactionDate(date)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse getTransaction(UUID id) throws ResourceNotFoundException {
        return transactionRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        Transaction transaction = transactionRepository.save(this.toEntity(request));
        return toResponse(transaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(UUID id, TransactionRequest request) throws ResourceNotFoundException {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));

        transaction.setValue(request.getValue());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setTransactionDate(request.getTransactionDate());

        return this.toResponse(transactionRepository.save(transaction));
    }

    public void deleteTransaction(UUID id) throws ResourceNotFoundException {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
        transactionRepository.delete(transaction);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return mapper.map(transaction, TransactionResponse.class);
    }

    private Transaction toEntity(TransactionRequest request) {
        return mapper.map(request, Transaction.class);
    }
}

