-- =============================================
-- VIETBANK DATABASE SCHEMA
-- Ngân hàng số Việt - Database Design
-- =============================================

-- Tạo database
DROP DATABASE IF EXISTS vietbank;
CREATE DATABASE IF NOT EXISTS vietbank CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE vietbank;

-- =============================================
-- 1. BẢNG ROLES - Vai trò người dùng
-- =============================================
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =============================================
-- 2. BẢNG USERS - Người dùng chung
-- =============================================
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- =============================================
-- 3. BẢNG CUSTOMERS - Thông tin khách hàng
-- =============================================
CREATE TABLE customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    date_of_birth DATE,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    citizen_id VARCHAR(20) UNIQUE,
    `address` TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =============================================
-- 4. BẢNG DEPARTMENTS - Phòng ban
-- Lưu ý: manager_id sẽ được thêm foreign key constraint sau khi tạo bảng staff
-- =============================================
CREATE TABLE departments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    -- manager_id INT NULL, -- Tham chiếu đến staff.id (sẽ thêm FK sau)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =============================================
-- 5. BẢNG POSITIONS - Chức vụ
-- =============================================
CREATE TABLE positions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    department_id INT NOT NULL,
    level INT DEFAULT 1 CHECK (level >= 1 AND level <= 10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);
-- Trong ngân hàng:
-- Level 1: Nhân viên cơ bản
-- Level 2: Chuyên viên
-- Level 3: Trưởng phòng
-- Level 4: Phó giám đốc
-- Level 5: Giám đốc
-- =============================================
-- 6. BẢNG STAFF - Thông tin nhân viên
-- =============================================
CREATE TABLE staff (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    employee_code VARCHAR(20) UNIQUE NOT NULL,
    department_id INT NOT NULL,
    position_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (position_id) REFERENCES positions(id)
);
-- =============================================
-- THÊM FOREIGN KEY CONSTRAINTS CHO CIRCULAR REFERENCES
-- =============================================

-- -- Thêm foreign key cho departments.manager_id (tham chiếu đến staff.id)
-- ALTER TABLE departments 
-- ADD CONSTRAINT fk_departments_manager 
-- FOREIGN KEY (manager_id) REFERENCES staff(id);


-- =============================================
-- 7. BẢNG ACCOUNT_TYPES - Loại tài khoản
-- =============================================
CREATE TABLE account_types (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(255),
    interest_rate DECIMAL(5,2) DEFAULT 0.00,
    minimum_balance DECIMAL(15,2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =============================================
-- 8. BẢNG ACCOUNTS - Tài khoản ngân hàng
-- =============================================
CREATE TABLE accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INT NOT NULL,
    account_type_id INT NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00 CHECK (balance >= 0),
    `status` ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'CLOSED') DEFAULT 'ACTIVE',
    opened_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (account_type_id) REFERENCES account_types(id)
);

-- =============================================
-- 9. BẢNG TRANSFERS - Giao dịch chuyển khoản
-- =============================================
CREATE TABLE transfers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    transfer_code VARCHAR(20) NOT NULL UNIQUE,
    from_account_id INT NOT NULL,
    to_account_id INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    fee DECIMAL(15,2) DEFAULT 0.00 CHECK (fee >= 0),
    total_amount DECIMAL(15,2) NOT NULL CHECK (total_amount > 0),
    `description` TEXT,
    `status` ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'PENDING',
    created_by INT,
    processed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (from_account_id) REFERENCES accounts(id),
    FOREIGN KEY (to_account_id) REFERENCES accounts(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    CHECK (from_account_id != to_account_id)
);

-- =============================================
-- 10. BẢNG DEPOSITS - Giao dịch nộp tiền
-- =============================================
CREATE TABLE deposits (
    id INT PRIMARY KEY AUTO_INCREMENT,
    deposit_code VARCHAR(20) NOT NULL UNIQUE,
    account_id INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    `description` TEXT,
    `status` ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'PENDING',
    created_by INT NOT NULL,
    processed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- =============================================
-- 11. BẢNG WITHDRAWALS - Giao dịch rút tiền
-- =============================================
CREATE TABLE withdrawals (
    id INT PRIMARY KEY AUTO_INCREMENT,
    withdrawal_code VARCHAR(20) NOT NULL UNIQUE,
    account_id INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    `description` TEXT,
    `status` ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'PENDING',
    created_by INT NOT NULL,
    processed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- =============================================
-- 12. BẢNG TRANSACTION_HISTORY - Lịch sử giao dịch tổng hợp
-- =============================================
CREATE TABLE transaction_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_code VARCHAR(20) NOT NULL UNIQUE,
    account_id INT NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT') NOT NULL,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    balance_before DECIMAL(15,2) NOT NULL CHECK (balance_before >= 0),
    balance_after DECIMAL(15,2) NOT NULL CHECK (balance_after >= 0),
    `description` TEXT,
    related_account_id INT NULL,
    related_transaction_id INT NULL,
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (related_account_id) REFERENCES accounts(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- =============================================
-- INSERT DỮ LIỆU MẪU
-- =============================================

-- Thêm các vai trò
INSERT INTO roles (name, description) VALUES 
('CUSTOMER', 'Khách hàng'),
('STAFF', 'Nhân viên ngân hàng'),
('ADMIN', 'Quản trị viên');

-- Thêm các phòng ban
INSERT INTO departments (name, description) VALUES 
('Giao dịch', 'Phòng giao dịch khách hàng'),
('Tín dụng', 'Phòng tín dụng và cho vay'),
('Kế toán', 'Phòng kế toán và tài chính'),
('IT', 'Phòng công nghệ thông tin'),
('Nhân sự', 'Phòng nhân sự và hành chính');

-- Thêm các chức vụ
INSERT INTO positions (name, description, department_id, level) VALUES 
-- Giao dịch
('Giao dịch viên', 'Nhân viên giao dịch cơ bản', (SELECT id FROM departments WHERE name = 'Giao dịch'), 1),
('Trưởng phòng giao dịch', 'Quản lý phòng giao dịch', (SELECT id FROM departments WHERE name = 'Giao dịch'), 3),
-- Tín dụng
('Chuyên viên tín dụng', 'Nhân viên phân tích tín dụng', (SELECT id FROM departments WHERE name = 'Tín dụng'), 2),
('Trưởng phòng tín dụng', 'Quản lý phòng tín dụng', (SELECT id FROM departments WHERE name = 'Tín dụng'), 3),
-- Kế toán
('Kế toán viên', 'Nhân viên kế toán', (SELECT id FROM departments WHERE name = 'Kế toán'), 1),
('Trưởng phòng kế toán', 'Quản lý phòng kế toán', (SELECT id FROM departments WHERE name = 'Kế toán'), 3),
-- IT
('Lập trình viên', 'Phát triển phần mềm', (SELECT id FROM departments WHERE name = 'IT'), 2),
('Trưởng phòng IT', 'Quản lý phòng IT', (SELECT id FROM departments WHERE name = 'IT'), 3),
-- Nhân sự
('Chuyên viên nhân sự', 'Quản lý nhân sự', (SELECT id FROM departments WHERE name = 'Nhân sự'), 2);

-- Thêm các loại tài khoản
INSERT INTO account_types (name, description, interest_rate, minimum_balance) VALUES 
('SAVINGS', 'Tài khoản tiết kiệm', 6.5, 100000),
('CHECKING', 'Tài khoản thanh toán', 0.0, 50000),
('BUSINESS', 'Tài khoản doanh nghiệp', 4.0, 1000000);

-- Thêm người dùng mẫu
INSERT INTO users (phone_number, password, role_id) VALUES
('0900000001', '123456', (SELECT id FROM roles WHERE name = 'CUSTOMER')),
('0900000002', '123456', (SELECT id FROM roles WHERE name = 'CUSTOMER')),
('0900000003', '123456', (SELECT id FROM roles WHERE name = 'STAFF')),
('0900000004', '123456', (SELECT id FROM roles WHERE name = 'STAFF')), -- Thêm nhân viên làm trưởng phòng
('0900000009', '123456', (SELECT id FROM roles WHERE name = 'ADMIN'));

-- Thêm khách hàng mẫu (tham chiếu users)
INSERT INTO customers (user_id, full_name, email, date_of_birth, gender, citizen_id, address)
VALUES
((SELECT id FROM users WHERE phone_number = '0900000001'), 'Nguyễn Văn A', 'a.nguyen@example.com', '1990-05-20', 'MALE', '012345678901', '123 Đường A, Quận 1, TP.HCM'),
((SELECT id FROM users WHERE phone_number = '0900000002'), 'Trần Thị B', 'b.tran@example.com', '1992-08-15', 'FEMALE', '012345678902', '456 Đường B, Quận 3, TP.HCM');

-- Thêm nhân viên mẫu (tham chiếu users)
INSERT INTO staff (user_id, full_name, email, employee_code, department_id, position_id)
VALUES
-- Nhân viên giao dịch
((SELECT id FROM users WHERE phone_number = '0900000003'), 'Lê Văn C', 'c.le@example.com', 'EMP001', 
 (SELECT id FROM departments WHERE name = 'Giao dịch'), 
 (SELECT id FROM positions WHERE name = 'Giao dịch viên')),
-- Trưởng phòng giao dịch
((SELECT id FROM users WHERE phone_number = '0900000004'), 'Phạm Thị D', 'd.pham@example.com', 'EMP002', 
 (SELECT id FROM departments WHERE name = 'Giao dịch'), 
 (SELECT id FROM positions WHERE name = 'Trưởng phòng giao dịch'));

-- -- Cập nhật phòng ban để có manager
-- UPDATE departments 
-- SET manager_id = (SELECT id FROM staff WHERE employee_code = 'EMP002') 
-- WHERE name = 'Giao dịch';

-- Thêm tài khoản mẫu cho khách hàng
INSERT INTO accounts (account_number, customer_id, account_type_id, balance, status)
VALUES
('1000000000001', (SELECT id FROM customers WHERE citizen_id = '012345678901'), (SELECT id FROM account_types WHERE name = 'CHECKING'), 1500000.00, 'ACTIVE'),
('1000000000002', (SELECT id FROM customers WHERE citizen_id = '012345678901'), (SELECT id FROM account_types WHERE name = 'SAVINGS'), 5000000.00, 'ACTIVE'),
('1000000000003', (SELECT id FROM customers WHERE citizen_id = '012345678902'), (SELECT id FROM account_types WHERE name = 'CHECKING'), 1200000.00, 'ACTIVE'),
('1000000000004', (SELECT id FROM customers WHERE citizen_id = '012345678902'), (SELECT id FROM account_types WHERE name = 'SAVINGS'), 800000.00, 'ACTIVE');

-- Thêm giao dịch nộp tiền mẫu
INSERT INTO deposits (deposit_code, account_id, amount, description, status, created_by)
VALUES
('DEP000001', (SELECT id FROM accounts WHERE account_number = '1000000000001'), 200000.00, 'Nộp tiền mặt', 'COMPLETED', (SELECT id FROM users WHERE phone_number = '0900000003')),
('DEP000002', (SELECT id FROM accounts WHERE account_number = '1000000000003'), 350000.00, 'Nộp tiền quầy', 'COMPLETED', (SELECT id FROM users WHERE phone_number = '0900000003'));

-- Thêm giao dịch chuyển khoản mẫu
INSERT INTO transfers (transfer_code, from_account_id, to_account_id, amount, fee, total_amount, description, status, created_by)
VALUES
('TRF000001', (SELECT id FROM accounts WHERE account_number = '1000000000001'), (SELECT id FROM accounts WHERE account_number = '1000000000003'), 300000.00, 3000.00, 303000.00, 'Chuyển tiền cho B', 'COMPLETED', (SELECT id FROM users WHERE phone_number = '0900000001')),
('TRF000002', (SELECT id FROM accounts WHERE account_number = '1000000000003'), (SELECT id FROM accounts WHERE account_number = '1000000000002'), 150000.00, 1500.00, 151500.00, 'Chuyển tiền tiết kiệm cho A', 'PENDING', (SELECT id FROM users WHERE phone_number = '0900000002'));

-- Thêm giao dịch rút tiền mẫu
INSERT INTO withdrawals (withdrawal_code, account_id, amount, description, status, created_by)
VALUES
('WTH000001', (SELECT id FROM accounts WHERE account_number = '1000000000001'), 200000.00, 'Rút tiền mặt', 'COMPLETED', (SELECT id FROM users WHERE phone_number = '0900000001')),
('WTH000002', (SELECT id FROM accounts WHERE account_number = '1000000000003'), 100000.00, 'Rút tiền ATM', 'PENDING', (SELECT id FROM users WHERE phone_number = '0900000002'));

-- Thêm lịch sử giao dịch mẫu
INSERT INTO transaction_history (transaction_code, account_id, transaction_type, amount, balance_before, balance_after, description, created_by)
VALUES
('HIS000001', (SELECT id FROM accounts WHERE account_number = '1000000000001'), 'DEPOSIT', 200000.00, 1300000.00, 1500000.00, 'Nộp tiền mặt', (SELECT id FROM users WHERE phone_number = '0900000003')),
('HIS000002', (SELECT id FROM accounts WHERE account_number = '1000000000001'), 'WITHDRAWAL', 200000.00, 1500000.00, 1300000.00, 'Rút tiền mặt', (SELECT id FROM users WHERE phone_number = '0900000001')),
('HIS000003', (SELECT id FROM accounts WHERE account_number = '1000000000001'), 'TRANSFER_OUT', 300000.00, 1300000.00, 1000000.00, 'Chuyển tiền cho B', (SELECT id FROM users WHERE phone_number = '0900000001')),
('HIS000004', (SELECT id FROM accounts WHERE account_number = '1000000000003'), 'TRANSFER_IN', 300000.00, 1200000.00, 1500000.00, 'Nhận tiền từ A', (SELECT id FROM users WHERE phone_number = '0900000001'));

-- =============================================
-- TẠO INDEX ĐỂ TỐI ƯU HIỆU SUẤT
-- =============================================

-- Index cho bảng users
CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_users_role ON users(role_id);

-- Index cho bảng customers
CREATE INDEX idx_customers_user ON customers(user_id);
CREATE INDEX idx_customers_citizen ON customers(citizen_id);
CREATE INDEX idx_customers_name ON customers(full_name);
CREATE INDEX idx_customers_deleted ON customers(is_deleted);

-- Index cho bảng departments
CREATE INDEX idx_departments_name ON departments(name);

-- Index cho bảng positions
CREATE INDEX idx_positions_department ON positions(department_id);
CREATE INDEX idx_positions_name ON positions(name);
CREATE INDEX idx_positions_level ON positions(level);

-- Index cho bảng staff
CREATE INDEX idx_staff_user ON staff(user_id);
CREATE INDEX idx_staff_employee_code ON staff(employee_code);
CREATE INDEX idx_staff_department ON staff(department_id);
CREATE INDEX idx_staff_position ON staff(position_id);
CREATE INDEX idx_staff_active ON staff(is_active);

-- Index cho bảng accounts
CREATE INDEX idx_accounts_customer ON accounts(customer_id);
CREATE INDEX idx_accounts_number ON accounts(account_number);
CREATE INDEX idx_accounts_type ON accounts(account_type_id);
CREATE INDEX idx_accounts_status ON accounts(status);

-- Index cho bảng transfers
CREATE INDEX idx_transfers_from ON transfers(from_account_id);
CREATE INDEX idx_transfers_to ON transfers(to_account_id);
CREATE INDEX idx_transfers_code ON transfers(transfer_code);
CREATE INDEX idx_transfers_status ON transfers(status);
CREATE INDEX idx_transfers_created ON transfers(created_at);

-- Index cho bảng deposits
CREATE INDEX idx_deposits_account ON deposits(account_id);
CREATE INDEX idx_deposits_code ON deposits(deposit_code);
CREATE INDEX idx_deposits_status ON deposits(status);
CREATE INDEX idx_deposits_created ON deposits(created_at);

-- Index cho bảng withdrawals
CREATE INDEX idx_withdrawals_account ON withdrawals(account_id);
CREATE INDEX idx_withdrawals_code ON withdrawals(withdrawal_code);
CREATE INDEX idx_withdrawals_status ON withdrawals(status);
CREATE INDEX idx_withdrawals_created ON withdrawals(created_at);

-- Index cho bảng transaction_history
CREATE INDEX idx_history_account ON transaction_history(account_id);
CREATE INDEX idx_history_type ON transaction_history(transaction_type);
CREATE INDEX idx_history_code ON transaction_history(transaction_code);
CREATE INDEX idx_history_created ON transaction_history(created_at);

-- =============================================
-- TẠO VIEW ĐỂ HỖ TRỢ TRUY VẤN
-- =============================================

-- View thông tin khách hàng đầy đủ
CREATE VIEW v_customer_info AS
SELECT 
    c.id as customer_id,
    c.full_name,
    c.email,
    c.citizen_id,
    c.gender,
    c.date_of_birth,
    c.address,
    u.phone_number,
    c.created_at
FROM customers c
JOIN users u ON c.user_id = u.id
WHERE c.is_deleted = FALSE;

-- View thông tin tài khoản với khách hàng
CREATE VIEW v_account_info AS
SELECT 
    a.id as account_id,
    a.account_number,
    a.balance,
    a.status,
    a.opened_date,
    c.full_name as customer_name,
    c.citizen_id,
    at.name as account_type_name,
    at.interest_rate
FROM accounts a
JOIN customers c ON a.customer_id = c.id
JOIN account_types at ON a.account_type_id = at.id
WHERE c.is_deleted = FALSE;

-- View giao dịch chuyển khoản chi tiết
CREATE VIEW v_transfer_details AS
SELECT 
    t.id as transfer_id,
    t.transfer_code,
    t.amount,
    t.fee,
    t.total_amount,
    t.description,
    t.status,
    t.created_at,
    fa.account_number as from_account,
    ta.account_number as to_account,
    fc.full_name as from_customer,
    tc.full_name as to_customer
FROM transfers t
JOIN accounts fa ON t.from_account_id = fa.id
JOIN accounts ta ON t.to_account_id = ta.id
JOIN customers fc ON fa.customer_id = fc.id
JOIN customers tc ON ta.customer_id = tc.id;

-- View lịch sử giao dịch tổng hợp
CREATE VIEW v_transaction_summary AS
SELECT 
    th.id as history_id,
    th.transaction_code,
    th.transaction_type,
    th.amount,
    th.balance_before,
    th.balance_after,
    th.description,
    th.created_at,
    a.account_number,
    c.full_name as customer_name,
    u.phone_number
FROM transaction_history th
JOIN accounts a ON th.account_id = a.id
JOIN customers c ON a.customer_id = c.id
JOIN users u ON c.user_id = u.id
ORDER BY th.created_at DESC;

-- View thông tin phòng ban với manager
CREATE VIEW v_department_info AS
SELECT 
    d.id as department_id,
    d.name as department_name,
    d.description as department_description,
    d.created_at,
    m.full_name as manager_name,
    m.employee_code as manager_code,
    m.email as manager_email,
    COUNT(s.id) as total_staff
FROM departments d
LEFT JOIN staff m ON d.id = m.department_id 
LEFT JOIN positions mp ON m.position_id = mp.id AND mp.level >= 3
LEFT JOIN staff s ON d.id = s.department_id
GROUP BY d.id, d.name, d.description, d.created_at, m.full_name, m.employee_code, m.email;

-- View thông tin nhân viên đầy đủ
CREATE VIEW v_staff_info AS
SELECT 
    s.id as staff_id,
    s.full_name,
    s.email,
    s.employee_code,
    s.is_active,
    s.created_at,
    u.phone_number,
    d.name as department_name,
    d.description as department_description,
    p.name as position_name,
    p.description as position_description,
    p.level as position_level,
    m.full_name as manager_name
FROM staff s
JOIN users u ON s.user_id = u.id
JOIN departments d ON s.department_id = d.id
JOIN positions p ON s.position_id = p.id
LEFT JOIN staff m ON d.id = m.department_id 
LEFT JOIN positions mp ON m.position_id = mp.id AND mp.level >= 3
WHERE s.is_active = TRUE;

-- View tổng hợp giao dịch theo tài khoản
CREATE VIEW v_account_transaction_summary AS
SELECT 
    a.id as account_id,
    a.account_number,
    c.full_name as customer_name,
    COUNT(th.id) as total_transactions,
    SUM(CASE WHEN th.transaction_type IN ('DEPOSIT', 'TRANSFER_IN') THEN th.amount ELSE 0 END) as total_incoming,
    SUM(CASE WHEN th.transaction_type IN ('WITHDRAWAL', 'TRANSFER_OUT') THEN th.amount ELSE 0 END) as total_outgoing,
    MAX(th.created_at) as last_transaction_date
FROM accounts a
JOIN customers c ON a.customer_id = c.id
LEFT JOIN transaction_history th ON a.id = th.account_id
GROUP BY a.id, a.account_number, c.full_name;
