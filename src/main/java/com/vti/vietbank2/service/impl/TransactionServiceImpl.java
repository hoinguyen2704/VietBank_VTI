package com.vti.vietbank2.service.impl;

import com.vti.vietbank2.dto.request.DepositRequest;
import com.vti.vietbank2.dto.request.TransferRequest;
import com.vti.vietbank2.dto.request.TransactionComplexSearchRequest;
import com.vti.vietbank2.dto.request.WithdrawalRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.PageResponse;
import com.vti.vietbank2.dto.response.TransactionDetailResponse;
import com.vti.vietbank2.dto.response.TransactionResponse;
import com.vti.vietbank2.entity.*;
import com.vti.vietbank2.entity.enums.TransactionType;
import com.vti.vietbank2.exception.InsufficientBalanceException;
import com.vti.vietbank2.exception.ResourceNotFoundException;
import com.vti.vietbank2.repository.*;
import com.vti.vietbank2.service.TransactionService;
import com.vti.vietbank2.util.AccountResolver;
import com.vti.vietbank2.util.SecurityContextHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final AccountResolver accountResolver;
    @Override
    @Transactional
    public ApiResponse<TransactionResponse> deposit(DepositRequest request) {
        // Use AccountResolver to find account by either accountId or accountNumber
        Account account = accountResolver.resolveAccount(request.getAccountId(), request.getAccountNumber());
        
        if (account.getStatus() != com.vti.vietbank2.entity.enums.AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active");
        }

        // Staff can deposit to any account, Customer cannot perform deposit
        // (Deposit typically done at bank counter by staff)

        // Validate staff exists and get user
        Staff staff = staffRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", request.getCreatedBy()));
        User user = staff.getUser();

        // Calculate new balance before creating transaction
        BigDecimal newBalance = account.getBalance().add(request.getAmount());

        // Create transaction record
        TransactionHistory transaction = new TransactionHistory();
        transaction.setTransaction_code(generateTransactionCode());
        transaction.setTransaction_type(TransactionType.DEPOSIT);
        transaction.setAccount_id(account);
        transaction.setAmount(request.getAmount());
        transaction.setBalance_before(account.getBalance());
        transaction.setBalance_after(newBalance); // Set balance_after immediately
        transaction.setDescription(request.getDescription());
        transaction.setCreated_by(user);
        transaction = transactionHistoryRepository.save(transaction);

        // Update account balance
        account.setBalance(newBalance);
        accountRepository.save(account);

        TransactionResponse response = convertToTransactionResponse(transaction);
        return ApiResponse.success("Deposit completed successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<TransactionResponse> withdraw(WithdrawalRequest request) {
        // Use AccountResolver to find account by either accountId or accountNumber
        Account account = accountResolver.resolveAccount(request.getAccountId(), request.getAccountNumber());
        
        if (account.getStatus() != com.vti.vietbank2.entity.enums.AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active");
        }

        // Check ownership - only allow CUSTOMER to withdraw from their own accounts
        String currentUsername = SecurityContextHelper.getCurrentUsername();
        if (currentUsername != null && SecurityContextHelper.isCustomer()) {
            // Verify that the account belongs to the current customer
            User currentUser = userRepository.findByPhoneNumber(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", currentUsername));
            
            Customer currentCustomer = customerRepository.findByUser_Id(currentUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "userId", currentUser.getId()));
            
            // Check if account belongs to current customer
            if (account.getCustomer().getId() != currentCustomer.getId()) {
                throw new IllegalArgumentException("You can only withdraw from your own accounts");
            }
        }

        // Check sufficient balance
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }

        // Validate staff exists and get user
        Staff staff = staffRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", request.getCreatedBy()));
        User user = staff.getUser();

        // Calculate new balance before creating transaction
        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());

        // Create transaction record
        TransactionHistory transaction = new TransactionHistory();
        transaction.setTransaction_code(generateTransactionCode());
        transaction.setTransaction_type(TransactionType.WITHDRAWAL);
        transaction.setAccount_id(account);
        transaction.setAmount(request.getAmount());
        transaction.setBalance_before(account.getBalance());
        transaction.setBalance_after(newBalance); // Set balance_after immediately
        transaction.setDescription(request.getDescription());
        transaction.setCreated_by(user);
        transaction = transactionHistoryRepository.save(transaction);

        // Update account balance
        account.setBalance(newBalance);
        accountRepository.save(account);

        TransactionResponse response = convertToTransactionResponse(transaction);
        return ApiResponse.success("Withdrawal completed successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<TransactionResponse> transfer(TransferRequest request) {
        // Use AccountResolver to find both accounts by either accountId or accountNumber
        Account fromAccount = accountResolver.resolveAccount(request.getFromAccountId(), request.getFromAccountNumber());
        
        if (fromAccount.getStatus() != com.vti.vietbank2.entity.enums.AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("From account is not active");
        }

        Account toAccount = accountResolver.resolveAccount(request.getToAccountId(), request.getToAccountNumber());
        
        if (toAccount.getStatus() != com.vti.vietbank2.entity.enums.AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("To account is not active");
        }

        // Check if accounts are different
        if (fromAccount.getId() == toAccount.getId()) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        // Check ownership - only allow CUSTOMER to transfer from their own accounts
        String currentUsername = SecurityContextHelper.getCurrentUsername();
        if (currentUsername != null && SecurityContextHelper.isCustomer()) {
            // Verify that the fromAccount belongs to the current customer
            User currentUser = userRepository.findByPhoneNumber(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", currentUsername));
            
            Customer currentCustomer = customerRepository.findByUser_Id(currentUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "userId", currentUser.getId()));
            
            // Check if fromAccount belongs to current customer
            if (fromAccount.getCustomer().getId() != currentCustomer.getId()) {
                throw new IllegalArgumentException("You can only transfer from your own accounts");
            }
        }

        // Validate staff exists and get user
        Staff staff = staffRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", request.getCreatedBy()));
        User user = staff.getUser();

        String transactionCode = generateTransactionCode();

        // Calculate new balances before creating transactions
        BigDecimal fromNewBalance = fromAccount.getBalance().subtract(request.getAmount());
        BigDecimal toNewBalance = toAccount.getBalance().add(request.getAmount());

        // Create transfer out transaction
        TransactionHistory transferOut = new TransactionHistory();
        transferOut.setTransaction_code(transactionCode);
        transferOut.setTransaction_type(TransactionType.TRANSFER_OUT);
        transferOut.setAccount_id(fromAccount);
        transferOut.setRelated_account_id(toAccount);
        transferOut.setAmount(request.getAmount());
        transferOut.setBalance_before(fromAccount.getBalance());
        transferOut.setBalance_after(fromNewBalance); // Set balance_after immediately
        transferOut.setDescription(request.getDescription());
        transferOut.setCreated_by(user);
        transferOut = transactionHistoryRepository.save(transferOut);
        transactionCode = generateTransactionCode();
        // Create transfer in transaction
        TransactionHistory transferIn = new TransactionHistory();
        transferIn.setTransaction_code(transactionCode);
        transferIn.setTransaction_type(TransactionType.TRANSFER_IN);
        transferIn.setAccount_id(toAccount);
        transferIn.setRelated_account_id(fromAccount);
        transferIn.setAmount(request.getAmount());
        transferIn.setBalance_before(toAccount.getBalance());
        transferIn.setBalance_after(toNewBalance); // Set balance_after immediately
        transferIn.setDescription(request.getDescription());
        transferIn.setCreated_by(user);
        transferIn = transactionHistoryRepository.save(transferIn);

        // Update account balances
        fromAccount.setBalance(fromNewBalance);
        toAccount.setBalance(toNewBalance);
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Link transactions
        transferOut.setRelated_transaction_id(transferIn.getId());
        transferIn.setRelated_transaction_id(transferOut.getId());
        
        transactionHistoryRepository.save(transferOut);
        transactionHistoryRepository.save(transferIn);

        // Return the transfer out transaction as primary response
        TransactionResponse response = convertToTransactionResponse(transferOut);
        return ApiResponse.success("Transfer completed successfully", response);
    }

    @Override
    public ApiResponse<TransactionResponse> getById(Integer id) {
        TransactionHistory transaction = transactionHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
        
        TransactionResponse response = convertToTransactionResponse(transaction);
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<List<TransactionResponse>> getByAccountId(Integer accountId) {
        List<TransactionHistory> transactions = transactionHistoryRepository.findByAccountId(accountId);
        List<TransactionResponse> responses = transactions.stream()
                .map(this::convertToTransactionResponse)
                .toList();
        return ApiResponse.success(responses);
    }

    @Override
    public ApiResponse<List<TransactionResponse>> getByAccountNumber(String accountNumber) {
        // Find account by account number
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountNumber));
        
        // Get all transactions for this account
        List<TransactionHistory> transactions = transactionHistoryRepository.findByAccountId(account.getId());
        List<TransactionResponse> responses = transactions.stream()
                .map(this::convertToTransactionResponse)
                .toList();
        return ApiResponse.success(responses);
    }

    @Override
    public ApiResponse<List<TransactionResponse>> getByCustomerId(Integer customerId) {
        // Get all accounts for the customer
        List<Account> accounts = accountRepository.findByCustomer_Id(customerId);
        List<Integer> accountIds = accounts.stream().map(Account::getId).toList();
        
        // Get all transactions for these accounts
        List<TransactionHistory> transactions = transactionHistoryRepository.findByAccountId_IdIn(accountIds);
        List<TransactionResponse> responses = transactions.stream()
                .map(this::convertToTransactionResponse)
                .toList();
        return ApiResponse.success(responses);
    }

    @Override
    public ApiResponse<Boolean> checkStaffExists(Integer staffId) {
        boolean exists = staffRepository.existsById(staffId);
        return ApiResponse.success(exists);
    }

    private String generateTransactionCode() {
        // Tạo transaction code duy nhất để phù hợp với VARCHAR(20)
        long timestamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        String code = "TXN" + (timestamp % 1000000000L) + uuid;
        return code.length() > 20 ? code.substring(0, 20) : code;
    }

    private TransactionResponse convertToTransactionResponse(TransactionHistory transaction) {
        // Get staff name from user
        String createdByName = getStaffNameFromUser(transaction.getCreated_by());
        
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransaction_code(),
                transaction.getTransaction_type(),
                transaction.getAccount_id().getId(),
                transaction.getAccount_id().getAccountNumber(),
                transaction.getAccount_id().getAccountName(), // Thêm accountName
                transaction.getAccount_id().getCustomer().getFullName(),
                transaction.getRelated_account_id() != null ? transaction.getRelated_account_id().getId() : null,
                transaction.getRelated_account_id() != null ? transaction.getRelated_account_id().getAccountNumber() : null,
                transaction.getRelated_account_id() != null ? transaction.getRelated_account_id().getAccountName() : null, // Thêm relatedAccountName
                transaction.getRelated_account_id() != null ? transaction.getRelated_account_id().getCustomer().getFullName() : null,
                transaction.getAmount(),
                BigDecimal.ZERO, // No fee field in current schema
                transaction.getBalance_before(),
                transaction.getBalance_after(),
                transaction.getDescription(),
                createdByName, // Staff full name or phone number as fallback
                transaction.getCreated_at(),
                transaction.getCreated_at() // No update field in current schema
        );
    }
    
    private String getStaffNameFromUser(User user) {
        // Find staff by user ID
        Optional<Staff> staffOptional = staffRepository.findByUser_Id(user.getId());
        if (staffOptional.isPresent()) {
            return staffOptional.get().getFullName();
        }
        // Fallback to phone number if staff not found
        return user.getPhoneNumber();
    }
    
    @Override
    public ApiResponse<PageResponse<TransactionDetailResponse>> searchComplexTransactions(TransactionComplexSearchRequest request) {
        // Tạo Pageable cho pagination và sorting
        Sort sort = Sort.by(
            request.getSortDirection().equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC, 
            request.getSortBy()
        );
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        
        // Thực hiện query phức tạp với nhiều JOIN
        Page<TransactionHistory> transactionPage = transactionHistoryRepository.findComplexTransactionHistory(
            request.getCustomerName(),
            request.getCustomerPhone(),
            request.getCitizenId(),
            request.getAccountNumber(),
            request.getAccountId(),
            request.getRelatedAccountNumber(),
            request.getTransactionType(),
            request.getTransactionCode(),
            request.getMinAmount(),
            request.getMaxAmount(),
            request.getFromDate(),
            request.getToDate(),
            request.getCreatedByStaffId(),
            pageable
        );
        
        // Convert to DTO
        List<TransactionDetailResponse> responses = transactionPage.getContent().stream()
            .map(this::convertToTransactionDetailResponse)
            .toList();
        
        // Tạo PageResponse
        PageResponse<TransactionDetailResponse> pageResponse = new PageResponse<>(
            responses,
            transactionPage.getNumber(),
            transactionPage.getSize(),
            transactionPage.getTotalElements(),
            transactionPage.getTotalPages(),
            transactionPage.isFirst(),
            transactionPage.isLast()
        );
        
        return ApiResponse.success(pageResponse);
    }
    
    private TransactionDetailResponse convertToTransactionDetailResponse(TransactionHistory th) {
        Account account = th.getAccount_id();
        Customer customer = account.getCustomer();
        User customerUser = customer.getUser();
        
        // Related account info (if exists)
        Account relatedAccount = th.getRelated_account_id();
        Customer relatedCustomer = relatedAccount != null ? relatedAccount.getCustomer() : null;
        User relatedCustomerUser = relatedCustomer != null ? relatedCustomer.getUser() : null;
        
        // Staff info (created_by)
        User createdByUser = th.getCreated_by();
        Optional<Staff> staffOptional = staffRepository.findByUser_Id(createdByUser.getId());
        
        TransactionDetailResponse response = new TransactionDetailResponse();
        
        // Transaction info
        response.setTransactionId(th.getId());
        response.setTransactionCode(th.getTransaction_code());
        response.setTransactionType(th.getTransaction_type());
        response.setAmount(th.getAmount());
        response.setBalanceBefore(th.getBalance_before());
        response.setBalanceAfter(th.getBalance_after());
        response.setDescription(th.getDescription());
        response.setCreatedAt(th.getCreated_at());
        
        // Main Account info
        response.setAccountId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountName(account.getAccountName());
        response.setAccountBalance(account.getBalance());
        
        // Main Customer info
        response.setCustomerId(customer.getId());
        response.setCustomerName(customer.getFullName());
        response.setCustomerPhone(customerUser.getPhoneNumber());
        response.setCustomerEmail(customer.getEmail());
        response.setCustomerCitizenId(customer.getCitizenId());
        
        // Related Account info (for transfers)
        if (relatedAccount != null) {
            response.setRelatedAccountId(relatedAccount.getId());
            response.setRelatedAccountNumber(relatedAccount.getAccountNumber());
            response.setRelatedAccountName(relatedAccount.getAccountName());
        }
        
        // Related Customer info
        if (relatedCustomer != null && relatedCustomerUser != null) {
            response.setRelatedCustomerId(relatedCustomer.getId());
            response.setRelatedCustomerName(relatedCustomer.getFullName());
            response.setRelatedCustomerPhone(relatedCustomerUser.getPhoneNumber());
        }
        
        // Staff who created transaction
        if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            response.setCreatedByStaffId(staff.getId());
            response.setCreatedByStaffName(staff.getFullName());
            response.setCreatedByStaffCode(staff.getEmployeeCode());
            response.setCreatedByStaffEmail(staff.getEmail());
        } else {
            // Fallback to user info if staff not found
            response.setCreatedByStaffId(createdByUser.getId());
            response.setCreatedByStaffName(createdByUser.getPhoneNumber());
        }
        
        return response;
    }
}