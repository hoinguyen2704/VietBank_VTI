-- =============================================
-- VÍ DỤ SỬ DỤNG INDEX TRONG DATABASE VIETBANK
-- =============================================
-- File này chứa các ví dụ thực tế về cách sử dụng index
-- để tối ưu hiệu suất truy vấn

USE vietbank;

-- =============================================
-- VÍ DỤ 1: Tìm kiếm khách hàng theo số điện thoại
-- Sử dụng index: idx_users_phone
-- =============================================
-- Query này sẽ sử dụng index idx_users_phone để tìm nhanh user theo phone_number
-- Thay vì scan toàn bộ bảng users, MySQL sẽ dùng index để tìm trực tiếp

EXPLAIN SELECT 
    u.id,
    u.phone_number,
    c.full_name,
    c.email,
    c.citizen_id
FROM users u
JOIN customers c ON u.id = c.user_id
WHERE u.phone_number = '0900000001';
-- Kết quả EXPLAIN sẽ cho thấy "key: idx_users_phone" - index được sử dụng

-- =============================================
-- VÍ DỤ 2: Tìm tài khoản theo số tài khoản
-- Sử dụng index: idx_accounts_number
-- =============================================
-- Khi tìm kiếm tài khoản theo account_number (rất phổ biến trong ngân hàng)
-- Index giúp tìm kiếm nhanh chóng thay vì scan toàn bộ bảng

EXPLAIN SELECT 
    a.account_number,
    a.account_name,
    a.balance,
    a.status,
    c.full_name as customer_name
FROM accounts a
JOIN customers c ON a.customer_id = c.id
WHERE a.account_number = '1000000000001';
-- Index idx_accounts_number sẽ được sử dụng

-- =============================================
-- VÍ DỤ 3: Lấy lịch sử giao dịch của một tài khoản
-- Sử dụng index: idx_history_account và idx_history_created
-- =============================================
-- Query này sử dụng composite index để tìm giao dịch theo account và sắp xếp theo ngày
-- Rất quan trọng khi bảng transaction_history có nhiều dữ liệu

EXPLAIN SELECT 
    th.transaction_code,
    th.transaction_type,
    th.amount,
    th.balance_before,
    th.balance_after,
    th.created_at
FROM transaction_history th
WHERE th.account_id = 1
ORDER BY th.created_at DESC
LIMIT 10;
-- Index idx_history_account giúp filter nhanh theo account_id
-- Index idx_history_created giúp sắp xếp nhanh theo created_at

-- =============================================
-- VÍ DỤ 4: Tìm kiếm khách hàng theo CCCD
-- Sử dụng index: idx_customers_citizen
-- =============================================
-- Tìm kiếm khách hàng theo số căn cước công dân
-- Index giúp tìm kiếm nhanh và đảm bảo tính duy nhất

EXPLAIN SELECT 
    c.id,
    c.full_name,
    c.citizen_id,
    c.email,
    u.phone_number
FROM customers c
JOIN users u ON c.user_id = u.id
WHERE c.citizen_id = '012345677901';
-- Index idx_customers_citizen được sử dụng

-- =============================================
-- VÍ DỤ 5: Lọc giao dịch chuyển khoản theo trạng thái và ngày
-- Sử dụng index: idx_transfers_status và idx_transfers_created
-- =============================================
-- Tìm các giao dịch chuyển khoản đang pending trong ngày
-- Index giúp filter nhanh theo status và sắp xếp theo ngày

EXPLAIN SELECT 
    t.transfer_code,
    fa.account_number as from_account,
    ta.account_number as to_account,
    t.amount,
    t.status,
    t.created_at
FROM transfers t
JOIN accounts fa ON t.from_account_id = fa.id
JOIN accounts ta ON t.to_account_id = ta.id
WHERE t.status = 'PENDING'
  AND DATE(t.created_at) = CURDATE()
ORDER BY t.created_at DESC;
-- Index idx_transfers_status và idx_transfers_created được sử dụng

-- =============================================
-- VÍ DỤ 6: Tìm nhân viên theo mã nhân viên
-- Sử dụng index: idx_staff_employee_code
-- =============================================
-- Tìm kiếm nhân viên theo employee_code (mã nhân viên)
-- Index giúp tìm kiếm nhanh chóng

EXPLAIN SELECT 
    s.employee_code,
    s.full_name,
    s.email,
    d.name as department_name,
    p.name as position_name
FROM staff s
JOIN departments d ON s.department_id = d.id
JOIN positions p ON s.position_id = p.id
WHERE s.employee_code = 'EMP001';
-- Index idx_staff_employee_code được sử dụng

-- =============================================
-- VÍ DỤ 7: Lọc tài khoản theo trạng thái và loại tài khoản
-- Sử dụng index: idx_accounts_status và idx_accounts_type
-- =============================================
-- Tìm tất cả tài khoản đang active của một loại cụ thể
-- Index giúp filter nhanh theo status và account_type

EXPLAIN SELECT 
    a.account_number,
    a.account_name,
    a.balance,
    at.name as account_type_name,
    c.full_name as customer_name
FROM accounts a
JOIN account_types at ON a.account_type_id = at.id
JOIN customers c ON a.customer_id = c.id
WHERE a.status = 'ACTIVE'
  AND a.account_type_id = 1;
-- Index idx_accounts_status và idx_accounts_type được sử dụng

-- =============================================
-- VÍ DỤ 8: Tìm kiếm khách hàng theo tên (LIKE query)
-- Sử dụng index: idx_customers_name
-- =============================================
-- Tìm kiếm khách hàng theo tên (partial match)
-- Index có thể giúp tối ưu một phần cho prefix search

EXPLAIN SELECT 
    c.full_name,
    c.email,
    c.citizen_id,
    u.phone_number
FROM customers c
JOIN users u ON c.user_id = u.id
WHERE c.full_name LIKE 'NGUYEN%'
  AND c.is_deleted = FALSE;
-- Index idx_customers_name có thể được sử dụng cho prefix search
-- Index idx_customers_deleted cũng được sử dụng

-- =============================================
-- VÍ DỤ 9: Thống kê giao dịch theo khoảng thời gian
-- Sử dụng index: idx_history_created
-- =============================================
-- Thống kê giao dịch trong một khoảng thời gian
-- Index giúp range scan nhanh chóng

EXPLAIN SELECT 
    DATE(th.created_at) as transaction_date,
    th.transaction_type,
    COUNT(*) as transaction_count,
    SUM(th.amount) as total_amount
FROM transaction_history th
WHERE th.created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
  AND th.created_at < CURDATE()
GROUP BY DATE(th.created_at), th.transaction_type
ORDER BY transaction_date DESC;
-- Index idx_history_created giúp range scan hiệu quả

-- =============================================
-- VÍ DỤ 10: Tìm tất cả giao dịch của một khách hàng
-- Sử dụng index: idx_accounts_customer và idx_history_account
-- =============================================
-- Lấy tất cả giao dịch của một khách hàng thông qua các tài khoản của họ
-- Index giúp join và filter nhanh chóng

EXPLAIN SELECT 
    a.account_number,
    th.transaction_code,
    th.transaction_type,
    th.amount,
    th.created_at
FROM customers c
JOIN accounts a ON c.id = a.customer_id
JOIN transaction_history th ON a.id = th.account_id
WHERE c.citizen_id = '012345677901'
ORDER BY th.created_at DESC;
-- Index idx_accounts_customer và idx_history_account được sử dụng

-- =============================================
-- SO SÁNH CHI TIẾT: Query CÓ INDEX vs KHÔNG CÓ INDEX
-- =============================================

-- =============================================
-- VÍ DỤ 1: Tìm tài khoản theo số tài khoản
-- =============================================

-- ✅ QUERY CÓ INDEX (NHANH)
-- Tìm tài khoản theo account_number - CÓ index idx_accounts_number
EXPLAIN SELECT 
    a.account_number,
    a.account_name,
    a.balance,
    a.status
FROM accounts a
WHERE a.account_number = '10000000018406';
-- Kết quả EXPLAIN sẽ cho thấy:
-- - type: ref hoặc const (rất nhanh)
-- - key: idx_accounts_number
-- - rows: 1 (chỉ scan 1 row)
-- - Extra: Using index (sử dụng index)

-- ❌ QUERY KHÔNG CÓ INDEX (CHẬM)
-- Tìm tài khoản theo account_name - KHÔNG có index
EXPLAIN SELECT 
    a.account_number,
    a.account_name,
    a.balance,
    a.status
FROM accounts a
WHERE a.account_name = 'Hoang';
-- Kết quả EXPLAIN sẽ cho thấy:
-- - type: ALL (full table scan - chậm)
-- - key: NULL (không có index được sử dụng)
-- - rows: số lượng tất cả rows trong bảng (phải scan hết)
-- - Extra: Using where (phải filter từng row)

-- =============================================
-- VÍ DỤ 2: Tìm kiếm khách hàng theo số điện thoại
-- =============================================

-- ✅ QUERY CÓ INDEX (NHANH)
-- Tìm user theo phone_number - CÓ index idx_users_phone
EXPLAIN SELECT 
    u.id,
    u.phone_number,
    c.full_name,
    c.email
FROM users u
LEFT JOIN customers c ON u.id = c.user_id
WHERE u.phone_number = '0900000001';
-- Kết quả:
-- - type: ref
-- - key: idx_users_phone
-- - rows: 1

-- ❌ QUERY KHÔNG CÓ INDEX (CHẬM)
-- Giả sử tìm user theo password (không có index, không nên làm thực tế)
-- Lưu ý: Đây chỉ là ví dụ để minh họa, không nên query theo password
EXPLAIN SELECT 
    u.id,
    u.phone_number
FROM users u
WHERE u.password = 'some_hash_value';
-- Kết quả:
-- - type: ALL (full table scan)
-- - key: NULL
-- - rows: tất cả rows trong bảng users

-- =============================================
-- VÍ DỤ 3: Lọc giao dịch theo account_id
-- =============================================

-- ✅ QUERY CÓ INDEX (NHANH)
-- Lấy lịch sử giao dịch của một tài khoản - CÓ index idx_history_account
EXPLAIN SELECT 
    th.transaction_code,
    th.transaction_type,
    th.amount,
    th.created_at
FROM transaction_history th
WHERE th.account_id = 1
ORDER BY th.created_at DESC
LIMIT 10;
-- Kết quả:
-- - type: ref
-- - key: idx_history_account
-- - rows: số giao dịch của account đó (ít)
-- - Extra: Using filesort (có thể tối ưu thêm bằng composite index)

-- ❌ QUERY KHÔNG CÓ INDEX (CHẬM)
-- Giả sử tìm giao dịch theo description (không có index)
EXPLAIN SELECT 
    th.transaction_code,
    th.transaction_type,
    th.amount
FROM transaction_history th
WHERE th.description LIKE '%chuyển tiền%';
-- Kết quả:
-- - type: ALL (full table scan)
-- - key: NULL
-- - rows: tất cả rows trong bảng transaction_history
-- - Extra: Using where (phải scan và filter từng row)

-- =============================================
-- VÍ DỤ 4: Tìm nhân viên theo mã nhân viên
-- =============================================

-- ✅ QUERY CÓ INDEX (NHANH)
-- Tìm nhân viên theo employee_code - CÓ index idx_staff_employee_code
EXPLAIN SELECT 
    s.employee_code,
    s.full_name,
    s.email,
    d.name as department_name
FROM staff s
JOIN departments d ON s.department_id = d.id
WHERE s.employee_code = 'EMP001';
-- Kết quả:
-- - type: ref
-- - key: idx_staff_employee_code
-- - rows: 1

-- ❌ QUERY KHÔNG CÓ INDEX (CHẬM)
-- Tìm nhân viên theo email - KHÔNG có index
EXPLAIN SELECT 
    s.employee_code,
    s.full_name,
    s.email
FROM staff s
WHERE s.email = 'c.le@example.com';
-- Kết quả:
-- - type: ALL
-- - key: NULL
-- - rows: tất cả rows trong bảng staff

-- =============================================
-- VÍ DỤ 5: Lọc giao dịch chuyển khoản theo trạng thái
-- =============================================

-- ✅ QUERY CÓ INDEX (NHANH)
-- Lọc giao dịch theo status - CÓ index idx_transfers_status
EXPLAIN SELECT 
    t.transfer_code,
    t.amount,
    t.status,
    t.created_at
FROM transfers t
WHERE t.status = 'PENDING'
ORDER BY t.created_at DESC;
-- Kết quả:
-- - type: ref
-- - key: idx_transfers_status
-- - rows: số giao dịch có status PENDING

-- ❌ QUERY KHÔNG CÓ INDEX (CHẬM)
-- Giả sử lọc theo description - KHÔNG có index
EXPLAIN SELECT 
    t.transfer_code,
    t.amount,
    t.description
FROM transfers t
WHERE t.description LIKE '%thanh toán%';
-- Kết quả:
-- - type: ALL
-- - key: NULL
-- - rows: tất cả rows trong bảng transfers

-- =============================================
-- VÍ DỤ 6: So sánh hiệu suất với số lượng dữ liệu lớn
-- =============================================

-- Giả sử bảng transaction_history có 1,000,000 rows

-- ✅ VỚI INDEX (NHANH - vài milliseconds)
-- Tìm giao dịch của account_id = 1
SELECT COUNT(*) 
FROM transaction_history 
WHERE account_id = 1;
-- Sử dụng idx_history_account
-- Chỉ scan các rows có account_id = 1 (ví dụ: 100 rows)
-- Thời gian: ~5ms

-- ❌ KHÔNG CÓ INDEX (CHẬM - vài giây)
-- Nếu không có index, phải scan toàn bộ 1,000,000 rows
-- Thời gian: ~2000ms (chậm hơn 400 lần!)

-- =============================================
-- VÍ DỤ 7: Tạo index mới và so sánh trước/sau
-- =============================================

-- BƯỚC 1: Kiểm tra query CHƯA CÓ INDEX
-- Giả sử muốn tìm tài khoản theo balance (chưa có index)
EXPLAIN SELECT 
    account_number,
    account_name,
    balance
FROM accounts
WHERE balance > 1000000;
-- Kết quả: type = ALL, key = NULL (full table scan)

-- BƯỚC 2: Tạo index mới
-- CREATE INDEX idx_accounts_balance ON accounts(balance);

-- BƯỚC 3: Kiểm tra query SAU KHI CÓ INDEX
EXPLAIN SELECT 
    account_number,
    account_name,
    balance
FROM accounts
WHERE balance > 1000000;
-- Kết quả: type = range, key = idx_accounts_balance (sử dụng index)

-- =============================================
-- BẢNG SO SÁNH TỔNG QUAN
-- =============================================
/*
┌─────────────────────┬──────────────────┬──────────────────┐
│ Tiêu chí            │ CÓ INDEX         │ KHÔNG CÓ INDEX   │
├─────────────────────┼──────────────────┼──────────────────┤
│ Type trong EXPLAIN  │ ref, const, range│ ALL (full scan)  │
│ Key                 │ Tên index        │ NULL             │
│ Rows scanned        │ Rất ít (1-100)   │ Tất cả rows      │
│ Thời gian (1M rows) │ ~5-50ms          │ ~1000-5000ms     │
│ Độ phức tạp         │ O(log n) hoặc O(1)│ O(n)            │
│ Hiệu quả            │ ⭐⭐⭐⭐⭐        │ ⭐               │
└─────────────────────┴──────────────────┴──────────────────┘
*/

-- =============================================
-- CÁCH KIỂM TRA INDEX CÓ ĐƯỢC SỬ DỤNG
-- =============================================

-- 1. Sử dụng EXPLAIN để xem execution plan
EXPLAIN SELECT * FROM accounts WHERE account_number = '1000000000001';

-- 2. Sử dụng EXPLAIN FORMAT=JSON để xem chi tiết hơn
EXPLAIN FORMAT=JSON SELECT * FROM accounts WHERE account_number = '1000000000001';

-- 3. Kiểm tra index có tồn tại không
SHOW INDEX FROM accounts WHERE Key_name = 'idx_accounts_number';

-- 4. Xem thống kê về index
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    CARDINALITY,
    INDEX_TYPE
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'vietbank'
  AND TABLE_NAME = 'accounts'
  AND INDEX_NAME = 'idx_accounts_number';

-- =============================================
-- LƯU Ý QUAN TRỌNG VỀ INDEX
-- =============================================
-- 1. Index giúp tăng tốc SELECT queries nhưng làm chậm INSERT/UPDATE/DELETE
-- 2. Chỉ nên tạo index cho các cột thường xuyên được dùng trong WHERE, JOIN, ORDER BY
-- 3. Index trên cột có nhiều giá trị trùng lặp (low cardinality) ít hiệu quả
-- 4. Composite index (nhiều cột) có thể hiệu quả hơn single column index trong một số trường hợp
-- 5. Sử dụng EXPLAIN để kiểm tra xem index có được sử dụng hay không

-- =============================================
-- KIỂM TRA INDEX ĐƯỢC SỬ DỤNG
-- =============================================
-- Chạy lệnh sau để xem tất cả index trong database:
SHOW INDEX FROM accounts;
SHOW INDEX FROM transaction_history;
SHOW INDEX FROM customers;

-- Hoặc xem index của tất cả bảng:
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME,
    SEQ_IN_INDEX,
    NON_UNIQUE
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'vietbank'
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;

