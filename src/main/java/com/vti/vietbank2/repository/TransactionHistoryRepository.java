package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.TransactionHistory;
import com.vti.vietbank2.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer>, JpaSpecificationExecutor<TransactionHistory> {
    Optional<TransactionHistory> findByTransactionCode(String transactionCode);
    boolean existsByTransactionCode(String transactionCode);
    List<TransactionHistory> findByAccountId_Id(Integer accountId);
    List<TransactionHistory> findByTransactionType(TransactionType transactionType);
    List<TransactionHistory> findByCreatedBy_Id(Integer createdById);
    List<TransactionHistory> findByRelatedAccountId_Id(Integer relatedAccountId);
}
