package com.vti.vietbank.specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.vti.vietbank.entity.Account;
import com.vti.vietbank.entity.Account_;
import com.vti.vietbank.entity.enums.AccountStatus;

public class AccountSpecs {

    /**
     * Tìm kiếm theo số tài khoản (LIKE, case-insensitive)
     */
    public static Specification<Account> hasAccountNumber(String accountNumber) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Account_.ACCOUNT_NUMBER)), "%" + accountNumber.toLowerCase() + "%");
    }

    /**
     * Tìm kiếm theo tên tài khoản (LIKE, case-insensitive)
     */
    public static Specification<Account> hasAccountName(String accountName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Account_.ACCOUNT_NAME)), "%" + accountName.toLowerCase() + "%");
    }

    /**
     * Lọc theo customer ID
     */
    public static Specification<Account> hasCustomerId(Integer customerId) {
        return (root, query, cb) -> cb.equal(root.get(Account_.CUSTOMER).get("id"), customerId);
    }

    /**
     * Lọc theo account type ID
     */
    public static Specification<Account> hasAccountTypeId(Integer accountTypeId) {
        return (root, query, cb) -> cb.equal(root.get(Account_.ACCOUNT_TYPE).get("id"), accountTypeId);
    }

    /**
     * Lọc theo trạng thái tài khoản
     */
    public static Specification<Account> hasStatus(AccountStatus status) {
        return (root, query, cb) -> cb.equal(root.get(Account_.STATUS), status);
    }

    /**
     * Lọc số dư >= minBalance
     */
    public static Specification<Account> hasMinBalance(BigDecimal minBalance) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Account_.BALANCE), minBalance);
    }

    /**
     * Lọc số dư <= maxBalance
     */
    public static Specification<Account> hasMaxBalance(BigDecimal maxBalance) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Account_.BALANCE), maxBalance);
    }

    /**
     * Lọc số dư trong khoảng [min, max]
     */
    public static Specification<Account> hasBalanceBetween(BigDecimal minBalance, BigDecimal maxBalance) {
        return Specification.where(hasMinBalance(minBalance)).and(hasMaxBalance(maxBalance));
    }

    /**
     * Lọc ngày mở >= fromDate
     */
    public static Specification<Account> openedFrom(LocalDateTime fromDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Account_.OPENED_DATE), fromDate);
    }

    /**
     * Lọc ngày mở <= toDate
     */
    public static Specification<Account> openedTo(LocalDateTime toDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Account_.OPENED_DATE), toDate);
    }

    /**
     * Lọc ngày mở trong khoảng [from, to]
     */
    public static Specification<Account> openedBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return Specification.where(openedFrom(fromDate)).and(openedTo(toDate));
    }
}
