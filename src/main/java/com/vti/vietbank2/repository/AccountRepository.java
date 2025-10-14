package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.Account;
import com.vti.vietbank2.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findByCustomer_Id(Integer customerId);
    List<Account> findByStatus(AccountStatus status);
    List<Account> findByCustomer_IdAndStatus(Integer customerId, AccountStatus status);
}
