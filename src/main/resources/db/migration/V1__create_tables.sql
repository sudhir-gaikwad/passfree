-- Create customer table
CREATE TABLE customer (
    cust_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    state VARCHAR(100),
    city VARCHAR(100),
    zip VARCHAR(20),
    acc_num VARCHAR(50),
    created_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create account table
CREATE TABLE account (
    cust_id VARCHAR(10) NOT NULL,
    acc_num VARCHAR(50) PRIMARY KEY,
    balance_amt DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    created_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cust_id) REFERENCES customer(cust_id)
);

-- Create preference table
CREATE TABLE preference (
    cust_id VARCHAR(10) NOT NULL,
    type VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    data TEXT,
    created_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (cust_id, type),
    FOREIGN KEY (cust_id) REFERENCES customer(cust_id)
);

-- Create beneficiary table
CREATE TABLE beneficiary (
    cust_id VARCHAR(10) NOT NULL,
    beneficiary_id VARCHAR(10) NOT NULL,
    name VARCHAR(255) NOT NULL,
    acc_num VARCHAR(50) NOT NULL,
    country VARCHAR(100),
    state VARCHAR(100),
    city VARCHAR(100),
    zip VARCHAR(20),
    created_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (beneficiary_id),
    FOREIGN KEY (cust_id) REFERENCES customer(cust_id)
);

-- Create transaction table
CREATE TABLE transaction (
    cust_id VARCHAR(10) NOT NULL,
    id BIGINT NOT NULL AUTO_INCREMENT,
    from_acc_num VARCHAR(50) NOT NULL,
    to_acc_num VARCHAR(50) NOT NULL,
    amt_before DECIMAL(15,2) NOT NULL,
    amt_after DECIMAL(15,2) NOT NULL,
    transfer_amt DECIMAL(15,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    mfa_type VARCHAR(50),
    otp VARCHAR(10),
    created_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (cust_id) REFERENCES customer(cust_id),
    FOREIGN KEY (from_acc_num) REFERENCES account(acc_num)
);
