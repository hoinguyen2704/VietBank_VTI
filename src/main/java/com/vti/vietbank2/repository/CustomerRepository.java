package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByCitizenId(String citizenId);
    boolean existsByCitizenId(String citizenId);
    Optional<Customer> findByUser_Id(Integer userId);
    Optional<Customer> findByUser_PhoneNumber(String phoneNumber);
}
