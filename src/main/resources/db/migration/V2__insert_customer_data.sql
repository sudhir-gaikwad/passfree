-- Insert one customer
INSERT INTO customer (cust_id, name, email, phone, password, country, state, city, zip, created_by_ts, updated_by_ts) 
VALUES ('1001', 'John Doe', 'john.doe@example.com', '+1234567890', 'password123', 'USA', 'California', 'San Francisco', '94102', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert one account for the customer
INSERT INTO account (cust_id, acc_num, balance_amt, created_by_ts, updated_by_ts) 
VALUES ('1001', 'ACC001', 1000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert two beneficiaries for the customer
INSERT INTO beneficiary (cust_id, beneficiary_id, name, acc_num, country, state, city, zip, created_by_ts, updated_by_ts) 
VALUES ('1001', 'BEN001', 'Jane Smith', 'BEN_ACC001', 'USA', 'New York', 'New York City', '10001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO beneficiary (cust_id, beneficiary_id, name, acc_num, country, state, city, zip, created_by_ts, updated_by_ts) 
VALUES ('1001', 'BEN002', 'Robert Johnson', 'BEN_ACC002', 'USA', 'Texas', 'Austin', '73301', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert three preferences for the customer (all disabled)
INSERT INTO preference (cust_id, type, enabled, data, created_by_ts, updated_by_ts) 
VALUES ('1001', 'TOTP', FALSE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO preference (cust_id, type, enabled, data, created_by_ts, updated_by_ts) 
VALUES ('1001', 'SMS', FALSE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO preference (cust_id, type, enabled, data, created_by_ts, updated_by_ts) 
VALUES ('1001', 'EMAIL', FALSE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
