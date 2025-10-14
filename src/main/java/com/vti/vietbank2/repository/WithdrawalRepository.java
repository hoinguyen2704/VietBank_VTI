package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.Withdrawal;
import com.vti.vietbank2.entity.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Integer>, JpaSpecificationExecutor<Withdrawal> {
    Optional<Withdrawal> findByWithdrawalCode(String withdrawalCode);
    boolean existsByWithdrawalCode(String withdrawalCode);
    List<Withdrawal> findByAccountId_Id(Integer accountId);
    List<Withdrawal> findByStatus(WithdrawalStatus status);
    List<Withdrawal> findByCreatedBy_Id(Integer createdById);
}
