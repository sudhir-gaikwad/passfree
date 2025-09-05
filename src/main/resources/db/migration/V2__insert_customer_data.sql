-- Insert one customer
INSERT INTO customer (cust_id, name, email, phone, password, country, state, city, zip) 
VALUES ('1001', 'John Doe', 'john.doe@example.com', '+1234567890', 'password123', 'USA', 'California', 'San Francisco', '94102');

-- Insert one account for the customer
INSERT INTO account (cust_id, acc_num, balance_amt) 
VALUES ('1001', 'ACC001', 1000.00);

-- Insert two beneficiaries for the customer
INSERT INTO beneficiary (cust_id, beneficiary_id, name, acc_num, country, state, city, zip) 
VALUES ('1001', 'BEN001', 'Jane Smith', 'BEN_ACC001', 'USA', 'New York', 'New York City', '10001');

INSERT INTO beneficiary (cust_id, beneficiary_id, name, acc_num, country, state, city, zip) 
VALUES ('1001', 'BEN002', 'Robert Johnson', 'BEN_ACC002', 'USA', 'Texas', 'Austin', '73301');

-- Insert three preferences for the customer (all disabled)
INSERT INTO preference (cust_id, type, enabled, data) 
VALUES ('1001', 'TOTP', FALSE, NULL);

INSERT INTO preference (cust_id, type, enabled, data) 
VALUES ('1001', 'SMS', FALSE, NULL);

INSERT INTO preference (cust_id, type, enabled, data) 
VALUES ('1001', 'EMAIL', FALSE, NULL);
