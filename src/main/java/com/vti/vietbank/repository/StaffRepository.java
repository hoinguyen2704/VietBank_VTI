package com.vti.vietbank.repository;

import com.vti.vietbank.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {
    Optional<Staff> findByEmployeeCode(String employeeCode);
    boolean existsByEmployeeCode(String employeeCode);
    Optional<Staff> findByUser_Id(Long userId);
    List<Staff> findByDepartment_Id(Long departmentId);
    List<Staff> findByIsActiveTrue();
    long countByDepartment_IdAndIsActiveTrue(int departmentId);
}
