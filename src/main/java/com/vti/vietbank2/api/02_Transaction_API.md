# 💰 Transaction APIs

**Base URL**: `http://localhost:8080/api/transactions`

## 📋 Tổng quan
API quản lý giao dịch: Nạp tiền, Rút tiền, Chuyển khoản với bảo mật ownership.

## 📋 Danh sách API

### POST /api/transactions/deposit
**Nạp tiền**

**Authentication:** Required (STAFF only)

**Request:**
```json
{
  "accountNumber": "1000000000001",
  "amount": 1000000,
  "description": "Nạp tiền tại quầy",
  "createdBy": 3
}
```

**Response:**
```json
{
  "success": true,
  "message": "Deposit completed successfully",
  "data": {
    "id": 1,
    "transactionCode": "TXN123456789",
    "transactionType": "DEPOSIT",
    "accountNumber": "1000000000001",
    "amount": 1000000,
    "balanceAfter": 2000000,
    "createdByName": "LE VAN C"
  }
}
```

---

### POST /api/transactions/withdraw
**Rút tiền**

**Authentication:** Required (CUSTOMER own accounts, STAFF all accounts)

**Request:**
```json
{
  "accountNumber": "1000000000001",
  "amount": 500000,
  "description": "Rút tiền ATM",
  "createdBy": 1
}
```

**Response:**
```json
{
  "success": true,
  "message": "Withdrawal completed successfully",
  "data": {
    "id": 2,
    "transactionCode": "TXN987654321",
    "transactionType": "WITHDRAWAL",
    "accountNumber": "1000000000001",
    "amount": 500000,
    "balanceAfter": 1500000,
    "createdByName": "NGUYEN VAN A"
  }
}
```

---

### POST /api/transactions/transfer
**Chuyển khoản**

**Authentication:** Required (CUSTOMER from own accounts, STAFF all accounts)

**Request:**
```json
{
  "fromAccountNumber": "1000000000001",
  "toAccountNumber": "1000000000002",
  "amount": 300000,
  "description": "Chuyển khoản",
  "createdBy": 1
}
```

**Response:**
```json
{
  "success": true,
  "message": "Transfer completed successfully",
  "data": {
    "id": 3,
    "transactionCode": "TXN456789012",
    "transactionType": "TRANSFER_OUT",
    "accountNumber": "1000000000001",
    "relatedAccountNumber": "1000000000002",
    "amount": 300000,
    "balanceAfter": 1200000,
    "createdByName": "NGUYEN VAN A"
  }
}
```

---

### GET /api/transactions/{id}
**Chi tiết giao dịch**

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "transactionCode": "TXN123456789",
    "transactionType": "DEPOSIT",
    "accountNumber": "1000000000001",
    "amount": 1000000,
    "balanceAfter": 2000000,
    "createdAt": "2024-01-15T10:00:00"
  }
}
```

---

### GET /api/transactions/account/{accountId}
**Lịch sử theo account ID**

**Parameters:** `accountId` (Integer)

**Response:** List of TransactionResponse

---

### GET /api/transactions/account-number/{accountNumber}
**Lịch sử theo số tài khoản**

**Parameters:** `accountNumber` (String)

**Response:** List of TransactionResponse

---

### GET /api/transactions/customer/{customerId}
**Lịch sử theo customer**

**Parameters:** `customerId` (Integer)

**Response:** List of TransactionResponse

---
