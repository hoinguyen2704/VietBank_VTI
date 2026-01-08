package com.vti.vietbank.repository;

import com.vti.vietbank.entity.Account;
import com.vti.vietbank.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findByCustomer_Id(Long customerId);
    List<Account> findByStatus(AccountStatus status);
    List<Account> findByCustomer_IdAndStatus(Long customerId, AccountStatus status);
    Optional<Account> findByAccountNumberAndCustomerId(String accountNumber, Long customerId);
}
