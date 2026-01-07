package com.vti.vietbank.repository;

import com.vti.vietbank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByCitizenId(String citizenId);
    boolean existsByCitizenId(String citizenId);
    Optional<Customer> findByUser_Id(Long userId);
    Optional<Customer> findByUser_PhoneNumber(String phoneNumber);

}
