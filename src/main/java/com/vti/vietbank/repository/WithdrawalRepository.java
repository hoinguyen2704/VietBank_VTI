package com.vti.vietbank.repository;

import com.vti.vietbank.entity.Withdrawal;
import com.vti.vietbank.entity.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Integer>, JpaSpecificationExecutor<Withdrawal> {

    @Query("select w from Withdrawal w where w.withdrawal_code = :code")
    Optional<Withdrawal> findByWithdrawalCode(@Param("code") String withdrawalCode);

    @Query("select (count(w) > 0) from Withdrawal w where w.withdrawal_code = :code")
    boolean existsByWithdrawalCode(@Param("code") String withdrawalCode);

    @Query("select w from Withdrawal w where w.account_id.id = :accountId")
    List<Withdrawal> findByAccountId(@Param("accountId") Integer accountId);

    List<Withdrawal> findByStatus(WithdrawalStatus status);

    @Query("select w from Withdrawal w where w.createdBy.id = :createdById")
    List<Withdrawal> findByCreatedBy(@Param("createdById") Integer createdById);
}
