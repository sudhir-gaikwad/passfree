# Rule Engine for Transaction Processing

This document describes the implementation of a rule engine for transaction processing with OTP-based authentication.

## Rules Implementation

The rule engine implements the following rules for transaction processing:

1. **Amount-based Rule**: If the transaction amount is greater than 100, OTP-based authentication is required.
2. **Location-based Rule**: If the state or country sent in the transaction API is different from the customer's state and country, OTP-based authentication is required.
3. **Optional Fields Handling**: State and country fields are optional in transactions. If provided, they are checked against the rules.

## OTP Authentication Process

When OTP authentication is applied, the following steps are performed:

1. Generate a 6-digit OTP
2. Save it along with the transaction
3. Save the transaction with INITIATED status
4. Do not deduct the amount from the customer account balance

## API Endpoints

### Transfer Amount
```
POST /api/accounts/transfer
```

**Request Body:**
```json
{
  "sourceAccountNumber": "string",
  "beneficiaryAccountNumber": "string",
  "amount": 0,
  "operatingSystem": "string",
  "state": "string",
  "country": "string"
}
```

**Response (when OTP is required):**
```json
{
  "status": "INITIATED",
  "message": "OTP required for this transaction. Please verify OTP to complete transfer.",
  "transactionId": "string",
  "otp": "string"
}
```

**Response (when OTP is not required):**
```json
{
  "status": "SUCCESS",
  "message": "Transfer completed successfully",
  "transactionId": "string"
}
```

### Verify OTP
```
POST /api/accounts/verify-otp
```

**Request Body:**
```json
{
  "transactionId": "string",
  "otp": "string"
}
```

**Response (on success):**
```json
{
  "status": "SUCCESS",
  "message": "Transfer completed successfully",
  "transactionId": "string"
}
```

**Response (on failure):**
```json
{
  "status": "FAILED",
  "message": "Invalid OTP provided",
  "transactionId": "string"
}
```

## Implementation Details

### RuleEngineService
The `RuleEngineService` class contains the logic for evaluating transaction rules:

- `isOTPRequired()`: Evaluates if OTP is required based on the configured rules
- `generateOTP()`: Generates a 6-digit OTP for authentication
- `isLocationDifferent()`: Helper method to compare transaction location with customer location

### AccountService Modifications
The `AccountService` was modified to:

1. Integrate with the rule engine
2. Handle OTP-based transactions
3. Implement OTP verification and transaction completion

### Database Changes
The existing `Transaction` entity was used with minimal changes:
- The `otp` field is used to store the generated OTP
- The `status` field uses `TransactionStatus.INITIATED` when OTP is required
- The `mfaType` field indicates the type of authentication used

## Testing the Implementation

To test the rule engine:

1. **Amount-based rule**: Make a transaction with amount > 10 to trigger OTP requirement
2. **Location-based rule**: Make a transaction with a state/country different from the customer's registered location
3. **Normal transaction**: Make a transaction that doesn't match any rules to verify normal processing

## Security Considerations

- In a production environment, OTPs should be sent via secure channels (SMS, email) rather than returned in the API response
- OTPs should have expiration times
- Rate limiting should be implemented to prevent OTP brute force attacks
- Additional security measures like logging and monitoring should be implemented
