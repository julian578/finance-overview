package com.julianjacobs.financeoverview.controller;

import com.julianjacobs.financeoverview.controller.response.BalanceResponse;
import com.julianjacobs.financeoverview.entity.Transaction;
import com.julianjacobs.financeoverview.entity.TransactionType;
import com.julianjacobs.financeoverview.entity.dto.TransactionDto;
import com.julianjacobs.financeoverview.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/protected/transaction")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService service) {
        this.transactionService = service;
    }


    @PostMapping("/income")
    public ResponseEntity<Transaction> createIncome(@RequestBody TransactionDto dto, Principal principal) {

        return transactionService.createTransaction(dto, principal, TransactionType.INCOME);
    }



    @PostMapping("/expense")
    public ResponseEntity<Transaction> createExpense(@RequestBody TransactionDto dto, Principal principal) {

        return transactionService.createTransaction(dto, principal, TransactionType.EXPENSE);
    }

    @GetMapping("/income")
    public ResponseEntity<List<Transaction>> getAllIncomes(Principal principal) {

        return transactionService.getAllTransactions(principal, TransactionType.INCOME);
    }

    @GetMapping("/expense")
    public ResponseEntity<List<Transaction>> getAllExpenses(Principal principal) {

        return transactionService.getAllTransactions(principal, TransactionType.EXPENSE);
    }

    @GetMapping("/income/month")
    public ResponseEntity<List<Transaction>> getAllIncomesOfThisMonth(Principal principal) {
        return transactionService.getAllTransactionsOfThisMonth(principal, TransactionType.INCOME);
    }

    @GetMapping("/expense/month")
    public ResponseEntity<List<Transaction>> getAllExpensesOfThisMonth(Principal principal) {
        return transactionService.getAllTransactionsOfThisMonth(principal, TransactionType.EXPENSE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteIncomeById(@PathVariable(name = "id") Long id, Principal principal) {
        return transactionService.deleteTransactionById(id, principal);
    }

    @GetMapping("/balance/month")
    public ResponseEntity<BalanceResponse> getTotalBalanceOfCurrentMonth(Principal principal) {
        return transactionService.getTotalBalanceOfCurrentMonth(principal);
    }

}
