# PassFree Project Context

## Project Overview
PassFree is a Java-based Spring Boot application focused on passwordless authentication. The application appears to be a financial services platform that manages customer accounts, transactions, beneficiaries, and user preferences.

## Technology Stack
- **Framework**: Spring Boot 3.5.5
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: JPA/Hibernate
- **Database Migration**: Flyway
- **Testing**: JUnit 5 (Spring Boot Test)

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/cognizant/passfree/
│   │       ├── PassfreeApplication.java (Main application class)
│   │       └── entities/ (Domain entities)
│   │           ├── Customer.java
│   │           ├── Account.java
│   │           ├── Transaction.java
│   │           ├── Beneficiary.java
│   │           ├── Preference.java
│   │           └── PreferenceId.java
│   └── resources/
│       ├── application.yaml (Configuration)
│       └── db/
│           └── migration/
│               ├── V1__create_tables.sql (Database schema)
│               └── V2__insert_customer_data.sql (Sample data)
└── test/
    └── java/
        └── com/cognizant/passfree/
            └── PassfreeApplicationTests.java (Basic Spring context test)
```

## Domain Model
The application manages financial data with the following entities:

1. **Customer**: Represents a user with personal information, accounts, preferences, beneficiaries, and transactions
2. **Account**: Financial accounts linked to customers with balance information
3. **Transaction**: Records of fund transfers between accounts
4. **Beneficiary**: Recipients that a customer can send money to
5. **Preference**: User preferences for authentication methods (TOTP, SMS, EMAIL)

## Database Schema
The database uses MySQL with the following tables:
- `customer`: Stores customer information
- `account`: Customer account details
- `transaction`: Records of financial transactions
- `beneficiary`: List of beneficiaries for each customer
- `preference`: Authentication preferences for customers

## Configuration
The application connects to a MySQL database at `localhost:3306` with:
- Database name: `passfree`
- Username: `root`
- Password: `root`

## Build and Run Instructions
1. Ensure MySQL is running with the correct credentials
2. Create the `passfree` database in MySQL
3. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   or on Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

## Testing
The project includes basic Spring context loading tests. To run tests:
```bash
./mvnw test
```

## Development Notes
- Uses Lombok for reducing boilerplate code in entities
- Uses JPA annotations for ORM mapping
- Database schema is managed by Flyway migrations
- Follows standard Maven project structure