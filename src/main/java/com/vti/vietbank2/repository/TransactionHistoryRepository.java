package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.TransactionHistory;
import com.vti.vietbank2.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer>, JpaSpecificationExecutor<TransactionHistory> {

    @Query("select th from TransactionHistory th where th.transaction_code = :code")
    Optional<TransactionHistory> findByTransactionCode(@Param("code") String transactionCode);

    @Query("select (count(th) > 0) from TransactionHistory th where th.transaction_code = :code")
    boolean existsByTransactionCode(@Param("code") String transactionCode);

    @Query("select th from TransactionHistory th where th.account_id.id = :accountId")
    List<TransactionHistory> findByAccountId(@Param("accountId") Integer accountId);
    
    @Query("select th from TransactionHistory th where th.account_id.id = :accountId")
    List<TransactionHistory> findByAccountId_Id(@Param("accountId") Integer accountId);

    @Query("select th from TransactionHistory th where th.transaction_type = :type")
    List<TransactionHistory> findByTransactionType(@Param("type") TransactionType transactionType);

    @Query("select th from TransactionHistory th where th.created_by.id = :createdById")
    List<TransactionHistory> findByCreatedBy(@Param("createdById") Integer createdById);

    @Query("select th from TransactionHistory th where th.related_account_id.id = :relatedAccountId")
    List<TransactionHistory> findByRelatedAccountId(@Param("relatedAccountId") Integer relatedAccountId);
    
    @Query("select th from TransactionHistory th where th.account_id.id in :accountIds")
    List<TransactionHistory> findByAccountId_IdIn(@Param("accountIds") List<Integer> accountIds);
}
