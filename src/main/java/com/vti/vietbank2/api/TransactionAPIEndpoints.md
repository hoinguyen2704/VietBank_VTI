# Transaction API Endpoints

## Tổng quan
API này cung cấp các tính năng giao dịch tài chính bao gồm nạp tiền, rút tiền, chuyển khoản và xem lịch sử giao dịch.

**Base URL**: `http://localhost:8080/api/transactions`

## 📋 Danh sách API Endpoints

### 1. 💰 Nạp tiền (Deposit)
- **Method**: `POST`
- **Endpoint**: `/api/transactions/deposit`
- **Description**: Thực hiện giao dịch nạp tiền vào tài khoản
- **Request Body**:
```json
{
  "accountNumber": "string (required)",
  "amount": "BigDecimal (required, min: 1000)",
  "description": "string (optional)",
  "createdBy": "Integer (required - Staff ID)"
}
```
- **Validation Rules**:
  - `accountNumber`: Không được để trống
  - `amount`: Phải là số dương, tối thiểu 1,000 VND
  - `createdBy`: Không được null
- **Response**: `ApiResponse<TransactionResponse>`

### 2. 💸 Rút tiền (Withdrawal)
- **Method**: `POST`
- **Endpoint**: `/api/transactions/withdraw`
- **Description**: Thực hiện giao dịch rút tiền từ tài khoản
- **Request Body**:
```json
{
  "accountNumber": "string (required)",
  "amount": "BigDecimal (required, min: 1000)",
  "description": "string (optional)",
  "createdBy": "Integer (required - Staff ID)"
}
```
- **Validation Rules**:
  - `accountNumber`: Không được để trống
  - `amount`: Phải là số dương, tối thiểu 1,000 VND
  - `createdBy`: Không được null
- **Response**: `ApiResponse<TransactionResponse>`

### 3. 🔄 Chuyển khoản (Transfer)
- **Method**: `POST`
- **Endpoint**: `/api/transactions/transfer`
- **Description**: Thực hiện giao dịch chuyển khoản giữa hai tài khoản
- **Request Body**:
```json
{
  "fromAccountNumber": "string (required)",
  "toAccountNumber": "string (required)",
  "amount": "BigDecimal (required, min: 1000)",
  "description": "string (optional)",
  "createdBy": "Integer (required - Staff ID)"
}
```
- **Validation Rules**:
  - `fromAccountNumber`: Không được để trống
  - `toAccountNumber`: Không được để trống
  - `amount`: Phải là số dương, tối thiểu 1,000 VND
  - `createdBy`: Không được null
- **Response**: `ApiResponse<TransactionResponse>`

### 4. 📊 Xem lịch sử giao dịch

#### 4.1. Theo ID giao dịch
- **Method**: `GET`
- **Endpoint**: `/api/transactions/{id}`
- **Description**: Lấy thông tin chi tiết một giao dịch theo ID
- **Path Parameters**:
  - `id`: Integer (required) - ID của giao dịch
- **Response**: `ApiResponse<TransactionResponse>`

#### 4.2. Theo ID tài khoản
- **Method**: `GET`
- **Endpoint**: `/api/transactions/account/{accountId}`
- **Description**: Lấy danh sách giao dịch theo ID tài khoản
- **Path Parameters**:
  - `accountId`: Integer (required) - ID của tài khoản
- **Response**: `ApiResponse<List<TransactionResponse>>`

#### 4.3. Theo số tài khoản
- **Method**: `GET`
- **Endpoint**: `/api/transactions/account-number/{accountNumber}`
- **Description**: Lấy danh sách giao dịch theo số tài khoản
- **Path Parameters**:
  - `accountNumber`: String (required) - Số tài khoản
- **Response**: `ApiResponse<List<TransactionResponse>>`

#### 4.4. Lịch sử tài khoản (duplicate endpoint)
- **Method**: `GET`
- **Endpoint**: `/api/transactions/account/{accountNumber}/history`
- **Description**: Lấy lịch sử giao dịch theo số tài khoản (tương tự endpoint trên)
- **Path Parameters**:
  - `accountNumber`: String (required) - Số tài khoản
- **Response**: `ApiResponse<List<TransactionResponse>>`

#### 4.5. Theo ID khách hàng
- **Method**: `GET`
- **Endpoint**: `/api/transactions/customer/{customerId}`
- **Description**: Lấy danh sách giao dịch theo ID khách hàng
- **Path Parameters**:
  - `customerId`: Integer (required) - ID của khách hàng
- **Response**: `ApiResponse<List<TransactionResponse>>`

### 5. ✅ Kiểm tra nhân viên
- **Method**: `GET`
- **Endpoint**: `/api/transactions/staff/{staffId}/exists`
- **Description**: Kiểm tra nhân viên có tồn tại không
- **Path Parameters**:
  - `staffId`: Integer (required) - ID của nhân viên
- **Response**: `ApiResponse<Boolean>`

## 📝 Response Format

### ApiResponse Structure
```json
{
  "success": "boolean",
  "message": "string",
  "data": "T | null",
  "timestamp": "LocalDateTime"
}
```

### TransactionResponse Structure
```json
{
  "id": "Integer",
  "transactionCode": "String",
  "transactionType": "TransactionType (DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT)",
  "accountId": "Integer",
  "accountNumber": "String",
  "accountName": "String",
  "customerName": "String",
  "relatedAccountId": "Integer",
  "relatedAccountNumber": "String",
  "relatedAccountName": "String",
  "relatedCustomerName": "String",
  "amount": "BigDecimal",
  "fee": "BigDecimal",
  "balanceBefore": "BigDecimal",
  "balanceAfter": "BigDecimal",
  "description": "String",
  "createdByName": "String",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

## 🔧 Ví dụ sử dụng

### Nạp tiền
```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1000000000001",
    "amount": 1000000,
    "description": "Nạp tiền vào tài khoản",
    "createdBy": 1
  }'
```

### Rút tiền
```bash
curl -X POST http://localhost:8080/api/transactions/withdraw \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1000000000001",
    "amount": 500000,
    "description": "Rút tiền mặt",
    "createdBy": 1
  }'
```

### Chuyển khoản
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountNumber": "1000000000001",
    "toAccountNumber": "1000000000002",
    "amount": 300000,
    "description": "Chuyển khoản",
    "createdBy": 1
  }'
```

### Lấy lịch sử giao dịch theo số tài khoản
```bash
curl -X GET http://localhost:8080/api/transactions/account-number/1000000000001
```

### Lấy chi tiết giao dịch theo ID
```bash
curl -X GET http://localhost:8080/api/transactions/1
```

### Kiểm tra nhân viên tồn tại
```bash
curl -X GET http://localhost:8080/api/transactions/staff/1/exists
```

## ⚠️ Lưu ý quan trọng

### Validation Rules
- Tất cả các giao dịch đều yêu cầu `createdBy` (ID nhân viên xử lý)
- Số tiền tối thiểu cho mọi giao dịch là 1,000 VND
- Tài khoản nguồn và đích trong chuyển khoản phải khác nhau
- Tất cả request đều được validate bằng `@Valid`

### Business Rules
- Giao dịch chuyển khoản sẽ tạo 2 bản ghi: `TRANSFER_OUT` và `TRANSFER_IN`
- Tất cả giao dịch đều được ghi lại với mã giao dịch duy nhất
- Response luôn bao gồm `accountName` và `relatedAccountName` khi có
- Số dư được cập nhật tự động sau mỗi giao dịch

### Error Handling
- API sử dụng `GlobalExceptionHandler` để xử lý lỗi
- Trả về `ApiResponse` với `success: false` khi có lỗi
- Validation errors được trả về với message chi tiết

### Transaction Types
- `DEPOSIT`: Nạp tiền
- `WITHDRAWAL`: Rút tiền  
- `TRANSFER_OUT`: Chuyển tiền đi
- `TRANSFER_IN`: Nhận tiền chuyển đến

## 🔍 Endpoints Summary

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/deposit` | Nạp tiền | DepositRequest | TransactionResponse |
| POST | `/withdraw` | Rút tiền | WithdrawalRequest | TransactionResponse |
| POST | `/transfer` | Chuyển khoản | TransferRequest | TransactionResponse |
| GET | `/{id}` | Chi tiết giao dịch | - | TransactionResponse |
| GET | `/account/{accountId}` | Lịch sử theo ID tài khoản | - | List<TransactionResponse> |
| GET | `/account-number/{accountNumber}` | Lịch sử theo số tài khoản | - | List<TransactionResponse> |
| GET | `/account/{accountNumber}/history` | Lịch sử tài khoản | - | List<TransactionResponse> |
| GET | `/customer/{customerId}` | Lịch sử theo khách hàng | - | List<TransactionResponse> |
| GET | `/staff/{staffId}/exists` | Kiểm tra nhân viên | - | Boolean |