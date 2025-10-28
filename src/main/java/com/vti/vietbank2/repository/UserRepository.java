package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.phoneNumber = :phoneNumber")
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
