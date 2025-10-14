package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer>, JpaSpecificationExecutor<Position> {
    Optional<Position> findByName(String name);
    boolean existsByName(String name);
    List<Position> findByDepartment_Id(Integer departmentId);
    List<Position> findByLevelGreaterThanEqual(Integer level);
}
