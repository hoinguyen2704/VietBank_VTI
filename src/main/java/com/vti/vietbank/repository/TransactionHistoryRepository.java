package com.vti.vietbank.repository;

import com.vti.vietbank.entity.TransactionHistory;
import com.vti.vietbank.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>, JpaSpecificationExecutor<TransactionHistory> {

    @Query("select th from TransactionHistory th where th.transaction_code = :code")
    Optional<TransactionHistory> findByTransactionCode(@Param("code") String transactionCode);

    @Query("select (count(th) > 0) from TransactionHistory th where th.transaction_code = :code")
    boolean existsByTransactionCode(@Param("code") String transactionCode);

    @Query("select th from TransactionHistory th where th.account_id.id = :accountId")
    List<TransactionHistory> findByAccountId(@Param("accountId") Long accountId);
    
    @Query("select th from TransactionHistory th where th.account_id.id = :accountId")
    List<TransactionHistory> findByAccountId_Id(@Param("accountId") Long accountId);

    @Query("select th from TransactionHistory th where th.transaction_type = :type")
    List<TransactionHistory> findByTransactionType(@Param("type") TransactionType transactionType);

    @Query("select th from TransactionHistory th where th.created_by.id = :createdById")
    List<TransactionHistory> findByCreatedBy(@Param("createdById") Long createdById);

    @Query("select th from TransactionHistory th where th.related_account_id.id = :relatedAccountId")
    List<TransactionHistory> findByRelatedAccountId(@Param("relatedAccountId") Long relatedAccountId);
    
    @Query("select th from TransactionHistory th where th.account_id.id in :accountIds")
    List<TransactionHistory> findByAccountId_IdIn(@Param("accountIds") List<Long> accountIds);
    
    /**
     * Complex query với nhiều JOIN để lấy transaction history với đầy đủ thông tin
     * JOIN: TransactionHistory -> Account -> Customer -> User
     * LEFT JOIN: RelatedAccount -> Customer
     * JOIN: CreatedBy (User) -> Staff
     */
    @Query("SELECT DISTINCT th FROM TransactionHistory th " +
           "JOIN FETCH th.account_id a " +
           "JOIN FETCH a.customer c " +
           "JOIN FETCH c.user u " +
           "LEFT JOIN FETCH th.related_account_id ra " +
           "LEFT JOIN FETCH ra.customer rc " +
           "LEFT JOIN FETCH rc.user ru " +
           "JOIN FETCH th.created_by cb " +
           "LEFT JOIN FETCH cb.role r " +
           "WHERE " +
           "(:customerName IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND " +
           "(:customerPhone IS NULL OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :customerPhone, '%'))) AND " +
           "(:citizenId IS NULL OR LOWER(c.citizenId) LIKE LOWER(CONCAT('%', :citizenId, '%'))) AND " +
           "(:accountNumber IS NULL OR a.accountNumber = :accountNumber) AND " +
           "(:accountId IS NULL OR a.id = :accountId) AND " +
           "(:relatedAccountNumber IS NULL OR ra.accountNumber = :relatedAccountNumber) AND " +
           "(:transactionType IS NULL OR th.transaction_type = :transactionType) AND " +
           "(:transactionCode IS NULL OR th.transaction_code = :transactionCode) AND " +
           "(:minAmount IS NULL OR th.amount >= :minAmount) AND " +
           "(:maxAmount IS NULL OR th.amount <= :maxAmount) AND " +
           "(:fromDate IS NULL OR th.created_at >= :fromDate) AND " +
           "(:toDate IS NULL OR th.created_at <= :toDate) AND " +
           "(:createdByStaffId IS NULL OR cb.id = :createdByStaffId) AND " +
           "c.isDeleted = false " +
           "ORDER BY th.created_at DESC")
    Page<TransactionHistory> findComplexTransactionHistory(
        @Param("customerName") String customerName,
        @Param("customerPhone") String customerPhone,
        @Param("citizenId") String citizenId,
        @Param("accountNumber") String accountNumber,
        @Param("accountId") Integer accountId,
        @Param("relatedAccountNumber") String relatedAccountNumber,
        @Param("transactionType") TransactionType transactionType,
        @Param("transactionCode") String transactionCode,
        @Param("minAmount") java.math.BigDecimal minAmount,
        @Param("maxAmount") java.math.BigDecimal maxAmount,
        @Param("fromDate") java.time.LocalDateTime fromDate,
        @Param("toDate") java.time.LocalDateTime toDate,
        @Param("createdByStaffId") Integer createdByStaffId,
        Pageable pageable
    );
}
