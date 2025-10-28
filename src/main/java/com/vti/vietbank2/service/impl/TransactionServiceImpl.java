package com.vti.vietbank2.service.impl;

import com.vti.vietbank2.dto.request.DepositRequest;
import com.vti.vietbank2.dto.request.TransferRequest;
import com.vti.vietbank2.dto.request.WithdrawalRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.TransactionResponse;
import com.vti.vietbank2.entity.*;
import com.vti.vietbank2.entity.enums.TransactionType;
import com.vti.vietbank2.exception.InsufficientBalanceException;
import com.vti.vietbank2.exception.ResourceNotFoundException;
import com.vti.vietbank2.repository.*;
import com.vti.vietbank2.service.TransactionService;
import com.vti.vietbank2.util.AccountResolver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final AccountResolver accountResolver;
    @Override
    @Transactional
    public ApiResponse<TransactionResponse> deposit(DepositRequest request) {
        // Use AccountResolver to find account by either accountId or accountNumber
        Account account = accountResolver.resolveAccount(request.getAccountId(), request.getAccountNumber());
        
        if (account.getStatus() != com.vti.vietbank2.entity.enums.AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active");
        }

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
}