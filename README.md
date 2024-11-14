

# 📦 Inventory Management System

Welcome to the **Inventory Management System**! This demo Spring Boot application is designed to simplify the management of suppliers, products, and orders. It provides an efficient and secure solution for managing inventory, suppliers, customers, and transactions through well-structured RESTful APIs. Utilizing JWT-based authentication and role-based access control, this application ensures robust security while offering seamless operations for both administrators and users.


## 🌟 Key Features

* **🔐 Secure Access**:JWT-based authentication with role-based access.
* **📦 Product & Inventory**: Full CRUD for products and inventory tracking
* **🛒 Order Management**: Manage orders with real-time status tracking.
* **🗂 Supplier Management**: Streamlined supplier and product linkage for efficient procurement.
* **📤 Bulk CSV Import/Export**: Upload and download products and orders via CSV.
* **⚠️ Structured Error Handling**: JSON-formatted error responses for smooth integration.
* **📧 Notifications**: Email notifications for order updates and product availability.
* **🔍 Role-Based Control**: Fine-grained permissions, with various role assignments.
* **⚙️ Automated Testing**: Unit tests for robust security, authentication, and functionality.
* **🧹 Data Validation**: Strong input validation with Spring Boot.
* **💻 DevOps Ready**: Fast development,live reload and debugging.


## 💻 **Technology Stack**

| **Category**           | **Technologies**                                |
|------------------------|-------------------------------------------------|
| **Framework**          | Spring Boot                                     |
| **Security**           | Spring Security, JWT                            |
| **Database**           | MySQL, PostgreSQL, Spring Data JPA              |
| **Utilities**          | Lombok, ModelMapper, Jakarta Validation API     |
| **CSV Support**        | Apache Commons CSV, OpenCSV                     |
| **JSON Handling**      | Jackson Databind, Spring Boot Starter JSON      |
| **Testing**            | Mockito, Spring Security Test, Spring Boot Test |
| **Logging**            | SLF4J, Logback                                  |
| **Mailing**            | Spring Boot Starter Mail                        |
| **Validation**         | Hibernate Validator, Jakarta Validation API     |
| **DevOps**             | Spring Boot DevTools                            |
| **JWT Support**        | JJWT API, JJWT Impl, JJWT Jackson               |
| **Database Connector** | MySQL Connector/J, PostgreSQL Driver            |




## 🗂️ Project Structure

```
inventory-management-system/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── inventory/
│   │   │               ├── config/
│   │   │               │   └── SecurityConfig.java
│   │   │               │ 
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── CSVController.java
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
│   │   │               ├── enums/
│   │   │               │   └── RoleName.java
│   │   │               │
│   │   │               ├── exceptions/
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── JwtTokenException.java
│   │   │               │   ├── OrderNotFoundException.java
│   │   │               │   ├── ProductNotFoundException.java
│   │   │               │   └── SupplierNotFoundException.java
│   │   │               │
│   │   │               ├── model/
│   │   │               │   ├── Order.java
│   │   │               │   ├── Product.java
│   │   │               │   ├── Role.java
│   │   │               │   ├── SupplierProfile.java
│   │   │               │   └── User.java
│   │   │               │
│   │   │               ├── repo/
│   │   │               │   ├── OrderRepo.java
│   │   │               │   ├── ProductRepo.java
│   │   │               │   ├── RoleRepo.java
│   │   │               │   ├── SupplierRepo.java
│   │   │               │   └── UserRepo.java
│   │   │               │
│   │   │               ├── security/
│   │   │               │   ├── AuthenticationRequest.java
│   │   │               │   ├── AuthenticationResponse.java
│   │   │               │   ├── JwtRequestFilter.java
│   │   │               │   └── JwtUtil.java
│   │   │               │
│   │   │               │
│   │   │               ├── service/
│   │   │               │   ├── impl/
│   │   │               │   │   ├── CSVServiceImpl.java
│   │   │               │   │   ├── EmailServiceImpl.java
│   │   │               │   │   ├── OrderServiceImpl.java
│   │   │               │   │   ├── ProductServiceImpl.java
│   │   │               │   │   ├── SupplierServiceImpl.java
│   │   │               │   │   └── UserServiceImpl.java
│   │   │               │   │
│   │   │               │   ├── CSVService.java
│   │   │               │   ├── EmailService.java
│   │   │               │   ├── OrderService.java
│   │   │               │   ├── ProductService.java
│   │   │               │   ├── SupplierService.java
│   │   │               │   └── UserService.java
│   │   │               │   
│   │   │               ├── utils/
│   │   │               │   ├── CsvUtils.java
│   │   │               │   ├── CustomCustomerDetailsService.java
│   │   │               │   ├── CustomEmailMessage.java
│   │   │               │   ├── Databaselnitializer.java
│   │   │               │   ├── KeyGeneratorT.java
│   │   │               │   └── Rolelnitializer
│   │   │               │ 
│   │   │               └── InventoryManagementSystemApplication.java
│   │   └── resources/
│   │       ├── docs/
│   │       │   └──README.md
│   │       └── application.properties
│   │       
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── inventory/
│                       ├── controller/
│                       │   ├── AuthControllerTest.java
│                       │   ├── CSVControllerTest.java
│                       │   ├── OrderControllerTest.java
│                       │   ├── ProductControllerTest.java
│                       │   ├── SupplierControllerTest.java
│                       │   └── UserControllerTest.java
│                       │ 
│                       ├── service/
│                       │   ├── CSVServicelmplTest.java
│                       │   ├── EmailServicelmplTest.java
│                       │   ├── OrderServicelmplTest.java
│                       │   ├── ProductServicelmplTest.java
│                       │   ├── SupplierServicelmplTest.java
│                       │   └── UserServicelmplTest.java   
│                       │
│                       └── InventoryManagementSystemApplicationTests.java
│   
│   
├── LICENSE  
├── mvnw  
├── mvnw.cmd  
├── pom.xml
└── README.md
```



## 🔗 **Dependencies**

| **Dependency**                                  | **Usage**                                         |
|-------------------------------------------------|---------------------------------------------------|
| **spring-boot-starter-data-jpa**                | Database integration via JPA (MySQL, PostgreSQL). |
| **spring-boot-starter-security**                | JWT authentication and access control.            |
| **spring-boot-starter-web**                     | REST API and web server support.                  |
| **jjwt-api, jjwt-impl, jjwt-jackson**           | JWT creation, signing, and validation.            |
| **spring-boot-devtools**                        | Development-time features like auto-reload.       |
| **postgresql**                                  | PostgreSQL JDBC driver (if used).                 |
| **spring-boot-starter-validation**              | Input validation via annotations.                 |
| **lombok**                                      | Reduces boilerplate code (getters, setters).      |
| **spring-boot-starter-test**                    | Testing tools (JUnit, Mockito).                   |
| **jackson-databind**                            | JSON serialization and deserialization.           |
| **slf4j-api, logback-classic**                  | Logging framework integration.                    |
| **mockito-core**                                | Mocking framework for unit tests.                 |
| **mysql-connector-j**                           | MySQL JDBC driver.                                |
| **commons-csv, opencsv**                        | CSV parsing and generation.                       |
| **modelmapper**                                 | Object mapping between models.                    |
| **spring-boot-starter-mail**                    | Email notification support.                       |
| **mockito-inline**                              | Advanced mocking for final classes.               |
| **jakarta.validation-api, hibernate-validator** | Bean validation for inputs and entities.          |



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
## 📜 API Documentation
For detailed information, please refer to the [ Full API Documentation ](src/main/resources/docs/README.md).



   


## 👑 Contributor

🌟 **[Suman Bisunkhe](https://github.com/sumanbisunkhe)** ✨ Transforming ideas into solutions.

## 📄 License
This project is licensed under the [Apache License 2.0](LICENSE).


## 📧 **Contact**

Got questions or need support? Feel free to reach out! We're here to help.

**Email**: [sumanbisunkhe304@gmail.com](mailto:sumanbisunkhe304@gmail.com)


