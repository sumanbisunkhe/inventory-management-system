

# 📦 Inventory Management System

Welcome to the **Inventory Management System**! This demo Spring Boot application is designed to simplify the management of suppliers, products, and orders. It provides an efficient and secure solution for managing inventory, suppliers, customers, and transactions through well-structured RESTful APIs. Utilizing JWT-based authentication and role-based access control, this application ensures robust security while offering seamless operations for both administrators and users.

## 🌟 Key Features

- **🔐 Secure Authentication & Authorization**: JWT-based token authentication with role-based access control for Admin, Supplier, and Customer roles.
- **📦 Product Management**: Create, update, and search products, complete with inventory tracking.
- **🛒 Order Management**: Handle order creation, updates, and tracking from placement to delivery.
- **🗂 Supplier Management**: Manage suppliers and their associated products with ease.
- **📤 CSV Import/Export**: Simplified CSV import/export for products and orders.
- **⚠️ Detailed Error Responses**: JSON responses for error handling, simplifying front-end integration.
- **📊 Reports & Analytics**: Generate insightful reports on inventory, orders, and suppliers.

## 💻 Technology Stack

| **Category**               | **Technologies**                             |
|----------------------------|----------------------------------------------|
| **Framework**              | Spring Boot                                  |
| **Security**               | Spring Security, JWT                         |
| **Database**               | MySQL, Spring Data JPA                       |
| **Utilities**              | Lombok, ModelMapper, Jakarta Validation API  |
| **CSV Support**            | Apache Commons CSV                           |
| **JSON Handling**          | Jackson Databind                             |

## 🗂️ Project Structure

```
inventory-management-system/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── inventory/
│   │   │           └── management/
│   │   │               ├── config/
│   │   │               │   └── SecurityConfig.java
│   │   │               │ 
│   │   │               ├── controllers/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── OrderController.java
│   │   │               │   ├── ProductController.java
│   │   │               │   ├── SupplierController.java
│   │   │               │   └── UserController.java
│   │   │               │
│   │   │               ├── dto/
│   │   │               │   ├── OrderDto.java
│   │   │               │   ├── ProductDto.java
│   │   │               │   ├── SupplierDto.java
│   │   │               │   └── UserDto.java
│   │   │               │
│   │   │               ├── entities/
│   │   │               │   ├── Order.java
│   │   │               │   ├── Product.java
│   │   │               │   ├── Role.java
│   │   │               │   ├── Supplier.java
│   │   │               │   └── User.java
│   │   │               │
│   │   │               ├── enums/
│   │   │               │   └── RoleName.java
│   │   │               │
│   │   │               ├── exceptions/
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               │
│   │   │               ├── jwt/
│   │   │               │   ├── JwtRequestFilter.java
│   │   │               │   └── JwtUtil.java
│   │   │               │
│   │   │               ├── repo/
│   │   │               │   ├── OrderRepo.java
│   │   │               │   ├── ProductRepo.java
│   │   │               │   ├── SupplierRepo.java
│   │   │               │   └── UserRepo.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   ├── impl/
│   │   │               │   │   ├── OrderServiceImpl.java
│   │   │               │   │   ├── ProductServiceImpl.java
│   │   │               │   │   ├── SupplierServiceImpl.java
│   │   │               │   │   └── UserServiceImpl.java
│   │   │               │   │
│   │   │               │   ├── OrderService.java
│   │   │               │   ├── ProductService.java
│   │   │               │   ├── SupplierService.java
│   │   │               │   └── UserService.java
│   │   │               │   
│   │   │               ├── utils/
│   │   │               │   └── CsvUtils.java
│   │   │               │ 
│   │   │               └── InventoryManagementSystemApplication.java
│   │   └── resources/
│   │       └── application.properties
│   │       
│   └── test/
│   
├── LICENSE  
├── mvnw  
├── mvnw.cmd  
├── pom.xml
└── README.md
```

## 🔗 Dependencies

### Core Spring Boot Dependencies
- **`spring-boot-starter-data-jpa`**: For ORM and database interactions.
- **`spring-boot-starter-security`**: Ensures robust authentication and authorization.
- **`spring-boot-starter-web`**: Facilitates building RESTful APIs and handling web requests.

### Development and Testing
- **`spring-boot-devtools`**: Automatic restarts during development.
- **`spring-boot-starter-test`**: JUnit and Mockito for effective testing.
- **`spring-security-test`**: Utilities for testing Spring Security.

### Database and Persistence
- **`mysql-connector-java`**: Driver for MySQL database connections.
- **`jakarta.validation-api`**: Ensures data integrity with bean validation.

### CSV Handling
- **`apache-commons-csv`**: Provides CSV parsing and exporting capabilities.

### JSON and Serialization
- **`jackson-databind`**: Core library for JSON processing.

### Authentication and Security
- **`jjwt`**: For secure token-based authentication using JSON Web Tokens (JWT).

### Utility Libraries
- **`lombok`**: Reduces boilerplate code.
- **`modelmapper`**: Simplifies mapping between DTOs and entities.

## 🚀 Usage

1. **Clone the Repository**
   ```bash
   git clone https://github.com/sumanbisunkhe/inventory-management-system.git
   cd inventory-management-system
   ```

2. **Database Setup**
   
   - Configure MySQL and update `application.properties` with your database credentials.
   ```properties
     spring.application.name=Inventory Management System
            
     # MySQL Database Configuration
     spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
            
     # Hibernate JPA Configuration
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.show-sql=true
     spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
            
     # Other Spring Boot Settings
     server.port=8080
     spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false
            
            
     # ========== JWT Configuration ==========
     jwt.secret=superSecretKeyHere
            
     # ========== Logging Configuration ==========
     logging.level.org.hibernate.SQL=DEBUG
     logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
            
     # ====== Mail Configuration ======
     spring.mail.host=smtp.gmail.com
     spring.mail.port=587
     spring.mail.username=you_email
     spring.mail.password=your_email_app_password
     spring.mail.properties.mail.smtp.auth=true
     spring.mail.properties.mail.smtp.starttls.enable=true
   ```

3. **Run Application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## 👑 Contributors

🌟 **[Suman Bisunkhe](https://github.com/sumanbisunkhe)** ✨ Transforming ideas into solutions.

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

