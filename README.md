

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
## API Documentation
>[ Inventory Management System API Documentation ](src/main/resources/swagger-api-docs/README.md)

The Swagger OpenAPI documentation is available at:
```
http://localhost:8080/swagger-ui.html
```

```json
  {
   "openapi": "3.0.1",
   "info": {
      "title": "Inventory Management System API",
      "description": "API documentation for managing inventory, suppliers, products, and orders",
      "contact": {
         "name": "Suman Bisunkhe",
         "url": "https://sumanbisunkhe.github.io/website/",
         "email": "sumanbisunkhe304@.com"
      },
      "license": {
         "name": "Apache 2.0",
         "url": "http://www.apache.org/licenses/LICENSE-2.0.html"
      },
      "version": "1.0.0"
   },
   "servers": [
      {
         "url": "http://localhost:8080",
         "description": "Generated server url"
      }
   ],
   "security": [
      {
         "bearerAuth": []
      }
   ],
   "paths": {
      "/api/users/update/{userId}": {
         "put": {
            "tags": [
               "user-controller"
            ],
            "summary": "Update user details",
            "description": "Updates the details of an existing user.",
            "operationId": "updateUser",
            "parameters": [
               {
                  "name": "userId",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "requestBody": {
               "content": {
                  "application/json": {
                     "schema": {
                        "$ref": "#/components/schemas/UserDto"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "200": {
                  "description": "User updated successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "400": {
                  "description": "Validation errors in user data",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "User not found with the provided ID",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/products/update/{productId}": {
         "put": {
            "tags": [
               "product-controller"
            ],
            "summary": "Update a product",
            "description": "Updates an existing product using its ID and the provided updated details.",
            "operationId": "updateProduct",
            "parameters": [
               {
                  "name": "productId",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "requestBody": {
               "content": {
                  "application/json": {
                     "schema": {
                        "$ref": "#/components/schemas/ProductDto"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "200": {
                  "description": "Product updated successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "400": {
                  "description": "Validation errors occurred",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "Product not found",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/orders/{id}": {
         "get": {
            "tags": [
               "order-controller"
            ],
            "summary": "Get an order by ID",
            "description": "Retrieves an order by its unique ID.",
            "operationId": "getOrderById",
            "parameters": [
               {
                  "name": "id",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "Order found",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "Order not found",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         },
         "put": {
            "tags": [
               "order-controller"
            ],
            "summary": "Update an existing order",
            "description": "Updates the order details for the specified order ID.",
            "operationId": "updateOrder",
            "parameters": [
               {
                  "name": "id",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "requestBody": {
               "content": {
                  "application/json": {
                     "schema": {
                        "$ref": "#/components/schemas/OrderDto"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "200": {
                  "description": "Order updated successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "400": {
                  "description": "Validation errors",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "Order not found",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         },
         "delete": {
            "tags": [
               "order-controller"
            ],
            "summary": "Delete an order",
            "description": "Deletes an order with the specified ID.",
            "operationId": "deleteOrder",
            "parameters": [
               {
                  "name": "id",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "Order deleted successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "Order not found",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/users/register": {
         "post": {
            "tags": [
               "user-controller"
            ],
            "summary": "Register a new user",
            "description": "Registers a new user with the provided details.",
            "operationId": "registerUser",
            "requestBody": {
               "content": {
                  "application/json": {
                     "schema": {
                        "$ref": "#/components/schemas/UserDto"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "201": {
                  "description": "User registered successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "400": {
                  "description": "Validation errors in user data",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/users/deactivate/{userId}": {
         "post": {
            "tags": [
               "user-controller"
            ],
            "summary": "Deactivate user",
            "description": "Deactivates a user by their ID.",
            "operationId": "deactivateUser",
            "parameters": [
               {
                  "name": "userId",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "User deactivated successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "User not found with the provided ID",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/users/activate/{userId}": {
         "post": {
            "tags": [
               "user-controller"
            ],
            "summary": "Activate user",
            "description": "Activates a user by their ID.",
            "operationId": "activateUser",
            "parameters": [
               {
                  "name": "userId",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "User activated successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "User not found with the provided ID",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/products/create": {
         "post": {
            "tags": [
               "product-controller"
            ],
            "summary": "Create a new product",
            "description": "Creates a new product in the system with the provided details.",
            "operationId": "createProduct",
            "requestBody": {
               "content": {
                  "application/json": {
                     "schema": {
                        "$ref": "#/components/schemas/ProductDto"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "201": {
                  "description": "Product created successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "400": {
                  "description": "Validation errors occurred",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/orders": {
         "get": {
            "tags": [
               "order-controller"
            ],
            "summary": "Get all orders",
            "description": "Retrieves a list of all orders in the system.",
            "operationId": "getAllOrders",
            "responses": {
               "200": {
                  "description": "Orders retrieved successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "array",
                           "items": {
                              "$ref": "#/components/schemas/OrderDto"
                           }
                        }
                     }
                  }
               }
            }
         },
         "post": {
            "tags": [
               "order-controller"
            ],
            "summary": "Create a new order",
            "description": "Creates a new order from the provided order details.",
            "operationId": "createOrder",
            "requestBody": {
               "content": {
                  "application/json": {
                     "schema": {
                        "$ref": "#/components/schemas/OrderDto"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "201": {
                  "description": "Order created successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "400": {
                  "description": "Validation errors",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/csv/import/products": {
         "post": {
            "tags": [
               "CSV Operations"
            ],
            "summary": "Import Products from CSV",
            "description": "Imports products from a CSV file into the database.",
            "operationId": "importProductsFromCSV",
            "requestBody": {
               "description": "CSV file containing product data",
               "content": {
                  "multipart/form-data": {
                     "schema": {
                        "type": "string",
                        "format": "binary"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "200": {
                  "description": "Products imported successfully",
                  "content": {
                     "application/json": {
                        "schema": {
                           "$ref": "#/components/schemas/ProductDto"
                        }
                     }
                  }
               },
               "500": {
                  "description": "Failed to import products"
               }
            }
         }
      },
      "/api/csv/import/orders": {
         "post": {
            "tags": [
               "CSV Operations"
            ],
            "summary": "Import Orders from CSV",
            "description": "Imports orders from a CSV file into the database.",
            "operationId": "importOrdersFromCSV",
            "requestBody": {
               "description": "CSV file containing order data",
               "content": {
                  "multipart/form-data": {
                     "schema": {
                        "type": "string",
                        "format": "binary"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "200": {
                  "description": "Orders imported successfully",
                  "content": {
                     "application/json": {
                        "schema": {
                           "$ref": "#/components/schemas/OrderDto"
                        }
                     }
                  }
               },
               "500": {
                  "description": "Failed to import orders"
               }
            }
         }
      },
      "/api/csv/export/products": {
         "post": {
            "tags": [
               "CSV Operations"
            ],
            "summary": "Export Products to CSV",
            "description": "Exports all products from the database to a CSV file at the specified file path.",
            "operationId": "exportProductsToCSV",
            "parameters": [
               {
                  "name": "filePath",
                  "in": "query",
                  "required": true,
                  "schema": {
                     "type": "string"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "Products exported successfully"
               },
               "500": {
                  "description": "Internal server error"
               }
            }
         }
      },
      "/api/csv/export/orders": {
         "post": {
            "tags": [
               "CSV Operations"
            ],
            "summary": "Export Orders to CSV",
            "description": "Exports all orders from the database to a CSV file at the specified file path.",
            "operationId": "exportOrdersToCSV",
            "parameters": [
               {
                  "name": "filePath",
                  "in": "query",
                  "required": true,
                  "schema": {
                     "type": "string"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "Orders exported successfully"
               },
               "500": {
                  "description": "Internal server error"
               }
            }
         }
      },
      "/api/auth/login": {
         "post": {
            "tags": [
               "Authentication"
            ],
            "summary": "User login",
            "description": "Authenticate a user and return a JWT token upon successful login.",
            "operationId": "login",
            "requestBody": {
               "description": "User login credentials",
               "content": {
                  "application/json": {
                     "schema": {
                        "$ref": "#/components/schemas/AuthenticationRequest"
                     },
                     "example": {
                        "identifier": "user@example.com",
                        "password": "password123"
                     }
                  }
               },
               "required": true
            },
            "responses": {
               "200": {
                  "description": "Successful authentication",
                  "content": {
                     "application/json": {
                        "schema": {
                           "$ref": "#/components/schemas/AuthenticationResponse"
                        },
                        "example": {
                           "jwtToken": "eyJhbGciOiJIUzI1NiIsInR...",
                           "message": "Login successful"
                        }
                     }
                  }
               },
               "401": {
                  "description": "Invalid credentials",
                  "content": {
                     "application/json": {
                        "example": "Invalid credentials"
                     }
                  }
               }
            }
         }
      },
      "/api/users/{userId}": {
         "get": {
            "tags": [
               "user-controller"
            ],
            "summary": "Get user by ID",
            "description": "Fetches the details of a user by their unique ID.",
            "operationId": "getUserById",
            "parameters": [
               {
                  "name": "userId",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "User details fetched successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "User not found with the provided ID",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/users/username/{username}": {
         "get": {
            "tags": [
               "user-controller"
            ],
            "summary": "Find user by username",
            "description": "Fetches a user by their username.",
            "operationId": "findByUsername",
            "parameters": [
               {
                  "name": "username",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "string"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "User details fetched successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "User not found with the provided username",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/users/email/{email}": {
         "get": {
            "tags": [
               "user-controller"
            ],
            "summary": "Find user by email",
            "description": "Fetches a user by their email.",
            "operationId": "findByEmail",
            "parameters": [
               {
                  "name": "email",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "string"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "User details fetched successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "User not found with the provided email",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/users/all": {
         "get": {
            "tags": [
               "user-controller"
            ],
            "summary": "Get all users",
            "description": "Fetches a list of all users in the system.",
            "operationId": "getAllUsers",
            "responses": {
               "200": {
                  "description": "List of users fetched successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "array",
                           "items": {
                              "$ref": "#/components/schemas/UserDto"
                           }
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/suppliers": {
         "get": {
            "tags": [
               "supplier-controller"
            ],
            "summary": "Get all suppliers",
            "description": "Fetches a list of all suppliers in the system.",
            "operationId": "getAllSuppliers",
            "responses": {
               "200": {
                  "description": "List of suppliers fetched successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "array",
                           "items": {
                              "$ref": "#/components/schemas/SupplierDto"
                           }
                        }
                     }
                  }
               },
               "500": {
                  "description": "Internal server error",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "array",
                           "items": {
                              "$ref": "#/components/schemas/SupplierDto"
                           }
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/suppliers/{id}": {
         "get": {
            "tags": [
               "supplier-controller"
            ],
            "summary": "Get supplier by ID",
            "description": "Fetches a supplier's details by their unique ID.",
            "operationId": "getSupplierById",
            "parameters": [
               {
                  "name": "id",
                  "in": "path",
                  "description": "ID of the supplier to retrieve",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "Supplier details fetched successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "$ref": "#/components/schemas/SupplierDto"
                        }
                     }
                  }
               },
               "404": {
                  "description": "Supplier not found with the provided ID",
                  "content": {
                     "*/*": {
                        "schema": {
                           "$ref": "#/components/schemas/SupplierDto"
                        }
                     }
                  }
               }
            }
         },
         "delete": {
            "tags": [
               "supplier-controller"
            ],
            "summary": "Delete supplier by ID",
            "description": "Deletes an existing supplier by their unique ID.",
            "operationId": "deleteSupplier",
            "parameters": [
               {
                  "name": "id",
                  "in": "path",
                  "description": "ID of the supplier to delete",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "Supplier deleted successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "string"
                        }
                     }
                  }
               },
               "404": {
                  "description": "Supplier not found with the provided ID",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "string"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/products/{productId}": {
         "get": {
            "tags": [
               "product-controller"
            ],
            "summary": "Get a product by ID",
            "description": "Retrieves a product's details by its ID.",
            "operationId": "getProductById",
            "parameters": [
               {
                  "name": "productId",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "Product retrieved successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               },
               "404": {
                  "description": "Product not found",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "object"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/products/all": {
         "get": {
            "tags": [
               "product-controller"
            ],
            "summary": "Get all products",
            "description": "Retrieves a list of all products in the system.",
            "operationId": "getAllProducts",
            "responses": {
               "200": {
                  "description": "List of products retrieved successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "array",
                           "items": {
                              "$ref": "#/components/schemas/ProductDto"
                           }
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/users/delete/{userId}": {
         "delete": {
            "tags": [
               "user-controller"
            ],
            "summary": "Delete user by ID",
            "description": "Deletes a user by their unique ID.",
            "operationId": "deleteUser",
            "parameters": [
               {
                  "name": "userId",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "User deleted successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "string"
                        }
                     }
                  }
               },
               "404": {
                  "description": "User not found with the provided ID",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "string"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api/products/delete/{productId}": {
         "delete": {
            "tags": [
               "product-controller"
            ],
            "summary": "Delete a product by ID",
            "description": "Deletes an existing product using its ID.",
            "operationId": "deleteProduct",
            "parameters": [
               {
                  "name": "productId",
                  "in": "path",
                  "required": true,
                  "schema": {
                     "type": "integer",
                     "format": "int64"
                  }
               }
            ],
            "responses": {
               "200": {
                  "description": "Product deleted successfully",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "string"
                        }
                     }
                  }
               },
               "404": {
                  "description": "Product not found",
                  "content": {
                     "*/*": {
                        "schema": {
                           "type": "string"
                        }
                     }
                  }
               }
            }
         }
      }
   },
   "components": {
      "schemas": {
         "UserDto": {
            "required": [
               "address",
               "dateOfBirth",
               "email",
               "fullName",
               "password",
               "phoneNumber",
               "roles",
               "username"
            ],
            "type": "object",
            "properties": {
               "id": {
                  "type": "integer",
                  "description": "Unique identifier for the user",
                  "format": "int64",
                  "example": 1
               },
               "username": {
                  "maxLength": 20,
                  "minLength": 3,
                  "type": "string",
                  "description": "Username for the user",
                  "example": "john_doe"
               },
               "password": {
                  "maxLength": 50,
                  "minLength": 8,
                  "type": "string",
                  "description": "Password for the user account (hashed)",
                  "example": "P@ssw0rd123"
               },
               "email": {
                  "type": "string",
                  "description": "Email address for the user",
                  "example": "john.doe@example.com"
               },
               "fullName": {
                  "maxLength": 100,
                  "minLength": 0,
                  "type": "string",
                  "description": "Full name of the user",
                  "example": "John Doe"
               },
               "roles": {
                  "uniqueItems": true,
                  "type": "array",
                  "description": "Roles assigned to the user",
                  "example": [
                     "CUSTOMER",
                     "SUPPLIER"
                  ],
                  "items": {
                     "type": "string",
                     "description": "Roles assigned to the user",
                     "example": "[\"CUSTOMER\",\"SUPPLIER\"]"
                  }
               },
               "dateOfBirth": {
                  "type": "string",
                  "description": "Date of birth of the user in dd-MM-yyyy format",
                  "format": "date",
                  "example": "0006-06-13"
               },
               "phoneNumber": {
                  "pattern": "^[+]?\\d{10,15}$",
                  "type": "string",
                  "description": "Phone number of the user",
                  "example": "+123456789"
               },
               "address": {
                  "maxLength": 255,
                  "minLength": 0,
                  "type": "string",
                  "description": "Address of the user",
                  "example": "1234 Elm Street, Springfield, IL"
               },
               "isActive": {
                  "type": "boolean",
                  "description": "Indicates if the user's account is active",
                  "example": true
               },
               "createdAt": {
                  "type": "string",
                  "description": "Timestamp when the user account was created",
                  "format": "date-time"
               },
               "updatedAt": {
                  "type": "string",
                  "description": "Timestamp when the user account was last updated",
                  "format": "date-time"
               }
            }
         },
         "ProductDto": {
            "required": [
               "name",
               "price",
               "stockQuantity",
               "supplierId"
            ],
            "type": "object",
            "properties": {
               "id": {
                  "type": "integer",
                  "description": "Unique identifier for the product",
                  "format": "int64",
                  "example": 1
               },
               "name": {
                  "maxLength": 100,
                  "minLength": 0,
                  "type": "string",
                  "description": "Name of the product",
                  "example": "Laptop"
               },
               "description": {
                  "maxLength": 500,
                  "minLength": 0,
                  "type": "string",
                  "description": "Description of the product",
                  "example": "15-inch laptop with 8GB RAM and 256GB SSD"
               },
               "price": {
                  "minimum": 0,
                  "exclusiveMinimum": true,
                  "type": "number",
                  "description": "Price of the product",
                  "example": 999.99
               },
               "stockQuantity": {
                  "minimum": 0,
                  "type": "integer",
                  "description": "Quantity of the product in stock",
                  "format": "int32",
                  "example": 50
               },
               "supplierId": {
                  "type": "integer",
                  "description": "Unique identifier of the supplier associated with this product",
                  "format": "int64",
                  "example": 1
               }
            }
         },
         "OrderDto": {
            "required": [
               "orderDate",
               "productIds",
               "totalAmount",
               "userId"
            ],
            "type": "object",
            "properties": {
               "id": {
                  "type": "integer",
                  "description": "Unique identifier for the order",
                  "format": "int64",
                  "example": 1
               },
               "orderDate": {
                  "type": "string",
                  "description": "Date and time when the order was placed",
                  "format": "date-time"
               },
               "totalAmount": {
                  "minimum": 0,
                  "exclusiveMinimum": true,
                  "type": "number",
                  "description": "Total amount for the order",
                  "example": 1999.99
               },
               "productIds": {
                  "type": "array",
                  "description": "List of product IDs included in the order",
                  "example": [
                     1,
                     2,
                     3
                  ],
                  "items": {
                     "type": "integer",
                     "description": "List of product IDs included in the order",
                     "format": "int64"
                  }
               },
               "userId": {
                  "type": "integer",
                  "description": "Unique identifier of the user who placed the order",
                  "format": "int64",
                  "example": 123
               }
            }
         },
         "AuthenticationRequest": {
            "required": [
               "identifier",
               "password"
            ],
            "type": "object",
            "properties": {
               "identifier": {
                  "type": "string",
                  "description": "User identifier which can be either username, email, or phone number",
                  "example": "johndoe@example.com"
               },
               "password": {
                  "type": "string",
                  "description": "User's password for authentication",
                  "example": "SecureP@ssw0rd"
               }
            }
         },
         "AuthenticationResponse": {
            "type": "object",
            "properties": {
               "jwt": {
                  "type": "string",
                  "description": "JWT token generated after successful authentication",
                  "example": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
               },
               "message": {
                  "type": "string",
                  "description": "Message indicating the result of the authentication process",
                  "example": "Authentication successful"
               }
            }
         },
         "SupplierDto": {
            "required": [
               "address",
               "contactNumber",
               "name"
            ],
            "type": "object",
            "properties": {
               "id": {
                  "type": "integer",
                  "description": "Unique identifier for the supplier",
                  "format": "int64",
                  "example": 1
               },
               "name": {
                  "maxLength": 100,
                  "minLength": 2,
                  "type": "string",
                  "description": "Name of the supplier",
                  "example": "Acme Corp"
               },
               "contactNumber": {
                  "pattern": "^\\+?[0-9]{7,15}$",
                  "type": "string",
                  "description": "Contact number of the supplier",
                  "example": "+1234567890"
               },
               "address": {
                  "maxLength": 255,
                  "minLength": 0,
                  "type": "string",
                  "description": "Physical address of the supplier",
                  "example": "456 Industrial Rd, Springfield, IL"
               }
            }
         }
      },
      "securitySchemes": {
         "bearerAuth": {
            "type": "http",
            "scheme": "bearer",
            "bearerFormat": "JWT"
         }
      }
   }
}
   
```

## 👑 Contributors

🌟 **[Suman Bisunkhe](https://github.com/sumanbisunkhe)** ✨ Transforming ideas into solutions.

## 📄 License
## License
This project is licensed under the [Apache License 2.0](LICENSE).

