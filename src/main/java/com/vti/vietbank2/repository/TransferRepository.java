package com.vti.vietbank2.repository;

import com.vti.vietbank2.entity.Transfer;
import com.vti.vietbank2.entity.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer>, JpaSpecificationExecutor<Transfer> {
    Optional<Transfer> findByTransferCode(String transferCode);
    boolean existsByTransferCode(String transferCode);
    List<Transfer> findByFromAccountId_Id(Integer fromAccountId);
    List<Transfer> findByToAccountId_Id(Integer toAccountId);
    List<Transfer> findByStatus(TransferStatus status);
    List<Transfer> findByCreatedBy_Id(Integer createdById);
}
