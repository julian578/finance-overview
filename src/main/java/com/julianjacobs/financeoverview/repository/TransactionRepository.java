package com.julianjacobs.financeoverview.repository;

import com.julianjacobs.financeoverview.entity.Transaction;
import com.julianjacobs.financeoverview.entity.TransactionType;
import com.julianjacobs.financeoverview.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {



    Transaction findBySubject(String subject);


    //returns transactions created after the creationDate handed over to the method
    @Query("select a from Transaction a where a.creationDate >= :creationDate and a.user = :user and a.transactionType = :transactionType")
    List<Transaction> findAllByCreationDateAfter(@Param("creationDate") Date creationDate, @Param("user") User user, @Param("transactionType") TransactionType transactionType);


    List<Transaction> findAllByUser(User user);


    List<Transaction> findAllByUserAndTransactionType(User user, TransactionType transactionType);


}
