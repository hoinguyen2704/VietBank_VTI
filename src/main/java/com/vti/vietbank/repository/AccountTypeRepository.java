package com.vti.vietbank.repository;

import com.vti.vietbank.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Integer>, JpaSpecificationExecutor<AccountType> {
    Optional<AccountType> findByName(String name);
    boolean existsByName(String name);
    List<AccountType> findByIsActivateTrue();
}
