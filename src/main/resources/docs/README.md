
# Inventory Management System API Documentation

This API allows you to manage inventory, suppliers, products, orders, and user accounts. It also includes features for CSV import/export and user authentication.

## Table of Contents
- [General Information](#general-information)
- [Getting Started](#getting-started)
- [Endpoints](#endpoints)
    - [User Controller](#user-controller)
    - [Product Controller](#product-controller)
    - [Order Controller](#order-controller)
    - [Supplier Controller](#supplier-controller)
    - [CSV Operations](#csv-operations)
    - [Authentication](#authentication)
- [Data Models](#data-models)
    - [UserDto](#userdto)
    - [ProductDto](#productdto)
    - [OrderDto](#orderdto)
    - [SupplierDto](#supplierdto)
    - [AuthenticationRequest](#authenticationrequest)
    - [AuthenticationResponse](#authenticationresponse)
- [License](#license)

## General Information
- **Title**: Inventory Management System API
- **Description**: API documentation for managing inventory, suppliers, products, and orders.
- **Version**: 1.0.0
- **Contact**: [Suman Bisunkhe](https://sumanbisunkhe.github.io/website/) (sumanbisunkhe304@gmail.com)
- **License**: [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

## Getting Started
- **Base URL**: `http://localhost:8080`
- **Authentication**: Bearer Token (JWT)

## Endpoints

### User Controller

| Method   | Endpoint                         | Summary               | Description                                 |
|----------|----------------------------------|-----------------------|---------------------------------------------|
| `POST`   | `/api/users/register`            | Register new user     | Registers a new user with provided details. |
| `PUT`    | `/api/users/update/{userId}`     | Update user details   | Updates details of an existing user.        |
| `POST`   | `/api/users/activate/{userId}`   | Activate user         | Activates a user by their ID.               |
| `POST`   | `/api/users/deactivate/{userId}` | Deactivate user       | Deactivates a user by their ID.             |
| `DELETE` | `/api/users/delete/{userId}`     | Delete user by ID     | Deletes a user by their unique ID.          |
| `GET`    | `/api/users/{userId}`            | Get user by ID        | Fetches the details of a user by unique ID. |
| `GET`    | `/api/users/username/{username}` | Find user by username | Fetches a user by their username.           |
| `GET`    | `/api/users/email/{email}`       | Find user by email    | Fetches a user by their email.              |
| `GET`    | `/api/users/all`                 | Get all users         | Fetches a list of all users in the system.  |

### Product Controller

| Method   | Endpoint                           | Summary              | Description                                   |
|----------|------------------------------------|----------------------|-----------------------------------------------|
| `POST`   | `/api/products/create`             | Create new product   | Creates a new product in the system.          |
| `PUT`    | `/api/products/update/{productId}` | Update product by ID | Updates a product with given ID.              |
| `DELETE` | `/api/products/delete/{productId}` | Delete product by ID | Deletes an existing product by ID.            |
| `GET`    | `/api/products/{productId}`        | Get product by ID    | Retrieves details of a product by ID.         |
| `GET`    | `/api/products/all`                | Get all products     | Fetches a list of all products in the system. |

### Order Controller

| Method   | Endpoint           | Summary            | Description                                   |
|----------|--------------------|--------------------|-----------------------------------------------|
| `POST`   | `/api/orders`      | Create new order   | Creates a new order with provided details.    |
| `PUT`    | `/api/orders/{id}` | Update order by ID | Updates order details for specified order ID. |
| `DELETE` | `/api/orders/{id}` | Delete order by ID | Deletes an order with specified ID.           |
| `GET`    | `/api/orders/{id}` | Get order by ID    | Retrieves an order by its unique ID.          |
| `GET`    | `/api/orders`      | Get all orders     | Fetches a list of all orders in the system.   |

### Supplier Controller

| Method   | Endpoint              | Summary               | Description                                    |
|----------|-----------------------|-----------------------|------------------------------------------------|
| `GET`    | `/api/suppliers`      | Get all suppliers     | Fetches a list of all suppliers in the system. |
| `GET`    | `/api/suppliers/{id}` | Get supplier by ID    | Fetches supplier details by unique ID.         |
| `DELETE` | `/api/suppliers/{id}` | Delete supplier by ID | Deletes an existing supplier by unique ID.     |

### CSV Operations

| Method | Endpoint                   | Summary                  | Description                                         |
|--------|----------------------------|--------------------------|-----------------------------------------------------|
| `POST` | `/api/csv/import/products` | Import products from CSV | Imports products from a CSV file into the database. |
| `POST` | `/api/csv/import/orders`   | Import orders from CSV   | Imports orders from a CSV file into the database.   |
| `POST` | `/api/csv/export/products` | Export products to CSV   | Exports all products to a specified CSV file path.  |
| `POST` | `/api/csv/export/orders`   | Export orders to CSV     | Exports all orders to a specified CSV file path.    |

### Authentication

| Method | Endpoint          | Summary    | Description                                              |
|--------|-------------------|------------|----------------------------------------------------------|
| `POST` | `/api/auth/login` | User login | Authenticates a user and returns a JWT token on success. |

## Data Models

### UserDto

The `UserDto` model represents user details. Note that certain fields (`id`, `isActive`, `createdAt`, and `updatedAt`) are automatically managed by the system.

```json
{
  "id": 1,
  "username": "john_doe",
  "password": "P@ssw0rd123",
  "email": "john.doe@example.com",
  "fullName": "John Doe",
  "roles": ["CUSTOMER", "SUPPLIER"],
  "dateOfBirth": "0006-06-13",
  "phoneNumber": "+123456789",
  "address": "1234 Elm Street, Springfield, IL",
  "isActive": true,
  "createdAt": "2024-11-11T15:20:48.747Z",
  "updatedAt": "2024-11-11T15:20:48.747Z"
}

```

### ProductDto

The `ProductDto` model represents the details of a product. Note that the `id` field is automatically managed by the system.

```json
{
  "id": 1,
  "name": "Laptop",
  "description": "15-inch laptop with 8GB RAM and 256GB SSD",
  "price": 999.99,
  "stockQuantity": 50,
  "supplierId": 1
}

```

### OrderDto

The `OrderDto` model represents an order in the system. Note that the `id`, `orderDate`, and `totalAmount` fields are automatically managed by the system.

```json
{
  "id": 1,
  "orderDate": "2024-01-01T12:00:00Z",
  "totalAmount": 1999.99,
  "productIds": [1, 2, 3],
  "userId": 123
}

```

### SupplierDto

The `SupplierDto` model represents the supplier details. Note that the `id`field is automatically managed by the system.

```json
{
  "id": 1,
  "name": "Acme Corp",
  "contactNumber": "+1234567890",
  "address": "456 Industrial Rd, Springfield, IL"
}
```

### AuthenticationRequest

The `AuthenticationRequest` model represents details required to generate jwt token for authentication. Note that the `identifier`field can be username, email or password.

```json
{
  "identifier": "johndoe@example.com",
  "password": "SecureP@ssw0rd"
}
```

### AuthenticationResponse

The `AuthenticationResponse` model represents response details after `AuthenticationRequest` . Note that the `identifier`field can be username, email or password.

```json
{
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Authentication successful"
}
```



This format help users easily navigate through the API documentation and understand each endpoint and data model.