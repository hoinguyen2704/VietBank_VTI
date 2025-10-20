package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer>, JpaSpecificationExecutor<Staff> {
    Optional<Staff> findByEmployeeCode(String employeeCode);
    boolean existsByEmployeeCode(String employeeCode);
    Optional<Staff> findByUser_Id(Integer userId);
    List<Staff> findByDepartment_Id(Integer departmentId);
    List<Staff> findByIsActiveTrue();
    long countByDepartment_IdAndIsActiveTrue(Integer departmentId);
}
