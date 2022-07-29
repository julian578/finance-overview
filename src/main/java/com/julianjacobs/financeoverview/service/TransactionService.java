package com.julianjacobs.financeoverview.service;

import com.julianjacobs.financeoverview.controller.response.BalanceResponse;
import com.julianjacobs.financeoverview.entity.Transaction;
import com.julianjacobs.financeoverview.entity.TransactionType;
import com.julianjacobs.financeoverview.entity.User;
import com.julianjacobs.financeoverview.entity.dto.TransactionDto;
import com.julianjacobs.financeoverview.repository.TransactionRepository;
import com.julianjacobs.financeoverview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }


    public ResponseEntity<Transaction> createTransaction(TransactionDto transactionDto,
                                                         Principal principal,
                                                         TransactionType transactionType) {
        try {

            Transaction transaction = new Transaction(transactionDto.getSubject(), transactionDto.getValue());
            transaction.setTransactionType(transactionType);

            Calendar c = Calendar.getInstance();

            transaction.setCreationDate(c.getTime());

            User user = getUser(principal);
            if(user != null) {
                transaction.setUser(user);

                transactionRepository.save(transaction);
                return new ResponseEntity<>(transaction, HttpStatus.OK);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //returns all Transactions of a certain type
    public ResponseEntity<List<Transaction>> getAllTransactions(Principal principal, TransactionType transactionType) {
        try {
            User user = getUser(principal);
            if(user != null) {
                return new ResponseEntity<>(transactionRepository.findAllByUserAndTransactionType(user, transactionType), HttpStatus.OK);
            }


        } catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //returns all Transactions of a certain type of this month
    public ResponseEntity<List<Transaction>> getAllTransactionsOfThisMonth(Principal principal, TransactionType transactionType) {
        try {

            User user = getUser(principal);
            if(user != null) {

                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, 1);

                return new ResponseEntity<>(transactionRepository.findAllByCreationDateAfter(c.getTime(), user, transactionType), HttpStatus.OK);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete Transaction by id
    public ResponseEntity deleteTransactionById(Long id, Principal principal) {

        try {
            //check if the Transaction has been created by the authenticated user
            User user = getUser(principal);
            if(user != null) {
                Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new Exception("Transaction not found"));
                if(transaction.getUser() == user) {
                    transactionRepository.deleteById(id);

                    return new ResponseEntity(HttpStatus.OK);
                }

            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    //returns the total balance of this month
    public ResponseEntity<BalanceResponse> getTotalBalanceOfCurrentMonth(Principal principal) {
        try {

            List<Transaction> expenses = getAllTransactionsOfThisMonth(principal, TransactionType.EXPENSE).getBody();
            List<Transaction> incomes = getAllTransactionsOfThisMonth(principal, TransactionType.INCOME).getBody();

            double balance = 0;

            for(Transaction e: expenses) {
                balance -= e.getValue();
            }

            for(Transaction i: incomes) {
                balance += i.getValue();
            }

            return new ResponseEntity<>(new BalanceResponse(balance), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //update subject or value of already created Transaction
    public ResponseEntity<Transaction> updateTransaction(Long id, TransactionDto transactionDto, Principal principal) {

        try {
            //check if the Transaction has been created by the authenticated user
            User user = getUser(principal);
            if(user != null) {
                Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new Exception("Transaction not found"));
                if (transaction.getUser() == user) {
                    transaction.setSubject(transactionDto.getSubject());
                    transaction.setValue(transactionDto.getValue());
                    transactionRepository.save(transaction);
                    return new ResponseEntity<>(transaction, HttpStatus.OK);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //helper function to extract User from Principal object
    private User getUser(Principal principal) {

        try {

            User user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new Exception("User not found"));

            return user;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
