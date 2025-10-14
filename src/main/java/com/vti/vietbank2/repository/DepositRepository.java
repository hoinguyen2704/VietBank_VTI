package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.Deposit;
import com.vti.vietbank2.entity.enums.DepositStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer>, JpaSpecificationExecutor<Deposit> {
    Optional<Deposit> findByDepositCode(String depositCode);
    boolean existsByDepositCode(String depositCode);
    List<Deposit> findByAccountId_Id(Integer accountId);
    List<Deposit> findByStatus(DepositStatus status);
    List<Deposit> findByCreatedBy_Id(Integer createdById);
}
