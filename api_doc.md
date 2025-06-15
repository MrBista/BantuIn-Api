# E-Commerce Service Provider API Documentation

## Overview
Comprehensive API for e-commerce service provider platform with role-based access control (RBAC) supporting multiple user types and product management.

**Base URL:** `http://localhost:9000/v1`

**Authentication:** Bearer Token (JWT)

## User Roles & Permissions

The system supports 4 user roles with different permission levels:

| Role | Description | Permissions |
|------|-------------|-------------|
| `SUPER_ADMIN` | System administrator | Full system access, can create ADMIN and SELLER roles |
| `ADMIN` | Platform administrator | Can manage users, products, can create SELLER role |
| `SELLER` | Product vendor | Can manage own products and orders |
| `CUSTOMER` | End user | Can browse products, place orders |

## Authentication

All API requests require authentication using Bearer tokens (except registration and login).

```http
Authorization: Bearer <your-token>
```

## Response Format

All responses follow this standard format:

```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {},
  "errors": null,
  "meta": {
    "timestamp": "2025-06-15T10:30:00Z",
    "version": "1.0",
    "requestId": "req_123456789"
  }
}
```

## Error Handling

Standard HTTP status codes are used:

- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden (Insufficient permissions)
- `404` - Not Found
- `422` - Validation Error
- `500` - Internal Server Error

Error response format:
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "errors": {
    "fieldName": ["Error message 1", "Error message 2"]
  },
  "meta": {
    "timestamp": "2025-06-15T10:30:00Z",
    "errorCode": "VALIDATION_FAILED"
  }
}
```

## Rate Limiting

- 1000 requests per hour per API key
- Rate limit headers included in response:
    - `X-RateLimit-Limit`
    - `X-RateLimit-Remaining`
    - `X-RateLimit-Reset`

---

# Authentication Endpoints

## Register User

Register a new customer account (automatically assigned CUSTOMER role).

**Endpoint:** `POST /auth/register`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "passwordConfirmation": "SecurePass123!",
  "phoneNumber": "+6281234567890",
  "dateOfBirth": "1990-05-15"
}
```

**Validation Rules:**
- `firstName` (string, required) - 2-50 characters
- `lastName` (string, required) - 2-50 characters
- `email` (string, required) - Valid email format, unique
- `password` (string, required) - Min 8 characters, must contain uppercase, lowercase, number, and special character
- `passwordConfirmation` (string, required) - Must match password
- `phoneNumber` (string, required) - Valid Indonesian phone number
- `dateOfBirth` (string, optional) - Format: YYYY-MM-DD

**Example Request:**
```http
POST /auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "passwordConfirmation": "SecurePass123!",
  "phoneNumber": "+6281234567890"
}
```

**Example Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "user": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "role": "CUSTOMER",
      "isActive": true,
      "emailVerified": false,
      "createdAt": "2025-06-15T10:30:00Z"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

## Login User

Authenticate user and receive access token.

**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

**Example Request:**
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

**Example Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "user": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "role": "CUSTOMER",
      "isActive": true,
      "lastLoginAt": "2025-06-15T10:30:00Z"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400
  }
}
```

## Logout User

Invalidate current access token.

**Endpoint:** `POST /auth/logout`

**Example Request:**
```http
POST /auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Example Response:**
```json
{
  "success": true,
  "message": "Logout successful",
  "data": null
}
```

---

# Users Endpoints (RBAC Protected)

## Get All Users

**Permissions:** SUPER_ADMIN, ADMIN

Retrieve a paginated list of all users with filtering options.

**Endpoint:** `GET /users`

**Query Parameters:**
- `page` (integer, optional) - Page number (default: 1)
- `limit` (integer, optional) - Items per page (default: 10, max: 100)
- `search` (string, optional) - Search users by name or email
- `role` (string, optional) - Filter by role (`SUPER_ADMIN`, `ADMIN`, `SELLER`, `CUSTOMER`)
- `isActive` (boolean, optional) - Filter by active status
- `sortBy` (string, optional) - Sort field (`firstName`, `lastName`, `email`, `createdAt`)
- `sortOrder` (string, optional) - Sort order (`asc`, `desc`)
- `include` (string, optional) - include additional information like stats and activity

**Example Request:**
```http
GET /users?page=1&limit=20&search=john&role=CUSTOMER&isActive=true&sortBy=createdAt&sortOrder=desc&include=stats,activity
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Example Response:**
```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": {
    "users": [
      {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "fullName" : "John Doe",
        "email": "john.doe@example.com",
        "role": "CUSTOMER",
        "status": "ACTIVE",
        "emailVerified": false,
        "phoneNumber": "628822232321",
        "avatar": "https://api.ecommerce-provider.com/uploads/avatars/user_1.jpg",
        "stats": {
          "totalOrders": 45,
          "totalSpent": 2450000,
          "avgOrderValue": 325000,
          "lastOrderDate": "2024-06-10T14:20:00Z",
          "lifetimeValue": 2850000,
          "customerTier": "gold",
          "favoriteCategories": ["electronics", "fashion"],
          "returnRate": 0.02,
          "daysSinceLastOrder": 5
        },
        "activity": {
          "lastLogin": "2024-06-14T09:15:00Z",
          "cartItems": 3,
          "wishlistItems": 12,
          "reviewsGiven": 28,
          "referralsMade": 3
        },
        "createdAt": "2025-01-15T10:30:00Z",
        "updatedAt": "2025-06-10T14:20:00Z",
        "lastLoginAt": "2025-06-15T09:15:00Z"
      }
    ],
    "pagination": {
      "currentPage": 1,
      "perPage": 20,
      "totalItems": 150,
      "totalPages": 8,
      "hasNext": true,
      "hasPrev": false
    }
  }
}
```

## Get User by ID

**Permissions:** SUPER_ADMIN, ADMIN, or own profile

**Endpoint:** `GET /users/{id}?include=stats, activity`

**Path Parameters:**
- `id` (integer, required) - User ID

**Query Parameters:**
- `include` (string, optional) - include additional information like stats and activity


**Example Response:**
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "user": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "fullName": "John Doe",
      "email": "john.doe@example.com",
      "role": "CUSTOMER",
      "status": "ACTIVE",
      "emailVerified": true,
      "phoneNumber": "+6281234567890",
      "dateOfBirth": "1990-05-15",
      "profileImage": "https://api.ecommerce-provider.com/uploads/avatars/user_1.jpg",
      "address": {
        "street": "Jl. Sudirman No. 123",
        "city": "Jakarta",
        "province": "DKI Jakarta",
        "postalCode": "12345",
        "country": "Indonesia"
      },
      "stats": {
        "totalOrders": 45,
        "totalSpent": 2450000,
        "avgOrderValue": 325000,
        "lastOrderDate": "2024-06-10T14:20:00Z",
        "lifetimeValue": 2850000,
        "customerTier": "gold",
        "favoriteCategories": ["electronics", "fashion"],
        "returnRate": 0.02,
        "daysSinceLastOrder": 5
      },
      "activity": {
        "lastLogin": "2024-06-14T09:15:00Z",
        "cartItems": 3,
        "wishlistItems": 12,
        "reviewsGiven": 28,
        "referralsMade": 3
      },
      "createdAt": "2025-01-15T10:30:00Z",
      "updatedAt": "2025-06-10T14:20:00Z",
      "lastLoginAt": "2025-06-15T09:15:00Z"
    }
  }
}
```

## Create User with Role

**Permissions:**
- SUPER_ADMIN: Can create any role
- ADMIN: Can create SELLER and CUSTOMER roles only

**Endpoint:** `POST /users`

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "username": "mrbista308",
  "password": "SecurePass123!",
  "passwordConfirmation": "SecurePass123!",
  "role": "SELLER",
  "phoneNumber": "+6281234567891"
}
```

**Validation Rules:**
- `firstName` (string, required) - 2-50 characters
- `lastName` (string, required) - 2-50 characters
- `username` (string, required) - 2-20 characters
- `email` (string, required) - Valid email format, unique
- `password` (string, required) - Min 8 characters with complexity requirements
- `passwordConfirmation` (string, required) - Must match password
- `role` (string, required) - Must be valid role based on user permissions
- `phoneNumber` (string, required) - Valid phone number

**Example Response:**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "user": {
      "id": 2,
      "firstName": "Jane",
      "lastName": "Smith",
      "email": "jane.smith@example.com",
      "role": "SELLER",
      "isActive": true,
      "emailVerified": false,
      "createdAt": "2025-06-15T10:30:00Z"
    }
  }
}
```

## Update User

**Permissions:** SUPER_ADMIN, ADMIN, or own profile (limited fields)

**Endpoint:** `PUT /users/{id}`

**Request Body:**
```json
{
  "fullName" : "john doe update",
  "phoneNumber": "+6281234567892",
  "status" : "Suspended"
}
```

**Example Response:**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "user": {
      "id": 1,
      "firstName": "John Updated",
      "lastName": "Doe Updated",
      "email": "john.doe@example.com",
      "role": "CUSTOMER",
      "status": "Suspended",
      "updatedAt": "2025-06-15T10:35:00Z",
      "createdAt": "2025-05-15T10:35:00Z"
    }
  }
}
```

## Change User Role

**Permissions:**
- SUPER_ADMIN: Can change to any role
- ADMIN: Can change CUSTOMER to SELLER only

**Endpoint:** `PATCH /users/{id}/role`

**Request Body:**
```json
{
  "role": "SELLER",
  "reason": "User requested seller privileges"
}
```

**Example Response:**
```json
{
  "success": true,
  "message": "User role updated successfully",
  "data": {
    "user": {
      "id": 1,
      "role": "SELLER",
      "updatedAt": "2025-06-15T10:40:00Z"
    }
  }
}
```

## Delete User

**Permissions:** SUPER_ADMIN only

**Endpoint:** `DELETE /users/{id}`

**Example Response:**
```json
{
  "success": true,
  "message": "User deleted successfully",
  "data": null
}
```

---

# Products Endpoints

## Get All Products

**Permissions:** All roles (public with different visibility)

**Endpoint:** `GET /products`

**Query Parameters:**
- `page` (integer, optional) - Page number (default: 1)
- `limit` (integer, optional) - Items per page (default: 10, max: 100)
- `search` (string, optional) - Search products by name or description
- `categoryId` (integer, optional) - Filter by category
- `sellerId` (integer, optional) - Filter by seller
- `minPrice` (number, optional) - Minimum price filter
- `maxPrice` (number, optional) - Maximum price filter
- `isActive` (boolean, optional) - Filter by active status (ADMIN/SUPER_ADMIN only)
- `sortBy` (string, optional) - Sort field (`name`, `price`, `createdAt`, `rating`)
- `sortOrder` (string, optional) - Sort order (`asc`, `desc`)

**Example Request:**
```http
GET /products?page=1&limit=20&search=laptop&categoryId=1&minPrice=500000&maxPrice=15000000&sortBy=price&sortOrder=asc
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Example Response:**
```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": {
    "products": [
      {
        "id": 1,
        "name": "Gaming Laptop ASUS ROG",
        "slug": "gaming-laptop-asus-rog",
        "description": "High-performance gaming laptop with RTX 4060",
        "shortDescription": "Gaming laptop with RTX 4060",
        "price": 12500000,
        "originalPrice": 15000000,
        "discountPercentage": 16.67,
        "sku": "ASUS-ROG-001",
        "stock": 25,
        "minOrder": 1,
        "maxOrder": 5,
        "weight": 2.5,
        "dimensions": {
          "length": 35.5,
          "width": 25.1,
          "height": 2.4
        },
        "isActive": true,
        "isFeatured": true,
        "images": [
          {
            "id": 1,
            "url": "https://api.ecommerce-provider.com/uploads/products/laptop_1_main.jpg",
            "altText": "ASUS ROG Gaming Laptop - Main View",
            "isPrimary": true
          }
        ],
        "category": {
          "id": 1,
          "name": "Electronics",
          "slug": "electronics"
        },
        "seller": {
          "id": 2,
          "firstName": "Jane",
          "lastName": "Smith",
          "shopName": "TechStore Indonesia",
          "rating": 4.8
        },
        "rating": {
          "average": 4.5,
          "totalReviews": 128
        },
        "tags": ["gaming", "laptop", "asus", "rtx"],
        "createdAt": "2025-02-15T10:30:00Z",
        "updatedAt": "2025-06-10T14:20:00Z"
      }
    ],
    "pagination": {
      "currentPage": 1,
      "perPage": 20,
      "totalItems": 450,
      "totalPages": 23,
      "hasNext": true,
      "hasPrev": false
    },
    "filters": {
      "categories": [
        {"id": 1, "name": "Electronics", "count": 120},
        {"id": 2, "name": "Fashion", "count": 85}
      ],
      "priceRange": {
        "min": 50000,
        "max": 50000000
      }
    }
  }
}
```

## Get Product by ID

**Permissions:** All roles

**Endpoint:** `GET /products/{id}`

**Path Parameters:**
- `id` (integer, required) - Product ID

**Example Response:**
```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "product": {
      "id": 1,
      "name": "Gaming Laptop ASUS ROG",
      "slug": "gaming-laptop-asus-rog",
      "description": "High-performance gaming laptop with RTX 4060. Perfect for gaming enthusiasts and content creators. Features include 16GB RAM, 512GB SSD, and 144Hz display.",
      "shortDescription": "Gaming laptop with RTX 4060",
      "price": 12500000,
      "originalPrice": 15000000,
      "discountPercentage": 16.67,
      "sku": "ASUS-ROG-001",
      "stock": 25,
      "minOrder": 1,
      "maxOrder": 5,
      "weight": 2.5,
      "dimensions": {
        "length": 35.5,
        "width": 25.1,
        "height": 2.4
      },
      "specifications": {
        "processor": "Intel Core i7-12700H",
        "memory": "16GB DDR4",
        "storage": "512GB NVMe SSD",
        "graphics": "NVIDIA RTX 4060 8GB",
        "display": "15.6\" FHD 144Hz",
        "os": "Windows 11 Home"
      },
      "isActive": true,
      "isFeatured": true,
      "images": [
        {
          "id": 1,
          "url": "https://api.ecommerce-provider.com/uploads/products/laptop_1_main.jpg",
          "altText": "ASUS ROG Gaming Laptop - Main View",
          "isPrimary": true
        },
        {
          "id": 2,
          "url": "https://api.ecommerce-provider.com/uploads/products/laptop_1_side.jpg",
          "altText": "ASUS ROG Gaming Laptop - Side View",
          "isPrimary": false
        }
      ],
      "category": {
        "id": 1,
        "name": "Electronics",
        "slug": "electronics",
        "parentId": null
      },
      "seller": {
        "id": 2,
        "firstName": "Jane",
        "lastName": "Smith",
        "shopName": "TechStore Indonesia",
        "shopDescription": "Trusted electronics retailer since 2020",
        "rating": 4.8,
        "totalProducts": 156,
        "joinedAt": "2020-03-15T10:30:00Z"
      },
      "rating": {
        "average": 4.5,
        "totalReviews": 128,
        "breakdown": {
          "5": 78,
          "4": 35,
          "3": 12,
          "2": 2,
          "1": 1
        }
      },
      "tags": ["gaming", "laptop", "asus", "rtx"],
      "seoMeta": {
        "title": "Gaming Laptop ASUS ROG - High Performance Gaming",
        "description": "Buy ASUS ROG Gaming Laptop with RTX 4060. Best price guarantee, fast shipping across Indonesia.",
        "keywords": "gaming laptop, asus rog, rtx 4060, gaming computer"
      },
      "createdAt": "2025-02-15T10:30:00Z",
      "updatedAt": "2025-06-10T14:20:00Z"
    }
  }
}
```

## Create Product

**Permissions:** SELLER (own products), ADMIN, SUPER_ADMIN

**Endpoint:** `POST /products`

**Request Body:**
```json
{
  "name": "Wireless Bluetooth Headphones",
  "description": "Premium wireless headphones with noise cancellation",
  "shortDescription": "Premium wireless headphones",
  "price": 599000,
  "originalPrice": 799000,
  "sku": "WH-BLUETOOTH-001",
  "stock": 50,
  "minOrder": 1,
  "maxOrder": 10,
  "weight": 0.3,
  "dimensions": {
    "length": 20,
    "width": 18,
    "height": 8
  },
  "categoryId": 1,
  "specifications": {
    "battery": "30 hours playback",
    "connectivity": "Bluetooth 5.0",
    "driver": "40mm dynamic drivers",
    "weight": "300g"
  },
  "tags": ["headphones", "wireless", "bluetooth", "noise-cancellation"],
  "seoMeta": {
    "title": "Wireless Bluetooth Headphones - Premium Audio",
    "description": "Shop premium wireless Bluetooth headphones with noise cancellation",
    "keywords": "wireless headphones, bluetooth, audio, music"
  }
}
```

**Validation Rules:**
- `name` (string, required) - 3-200 characters
- `description` (string, required) - 10-5000 characters
- `shortDescription` (string, optional) - Max 200 characters
- `price` (number, required) - Positive number
- `originalPrice` (number, optional) - Must be >= price
- `sku` (string, required) - Unique, 3-50 characters
- `stock` (integer, required) - Non-negative integer
- `minOrder` (integer, optional) - Default: 1
- `maxOrder` (integer, optional) - Must be >= minOrder
- `weight` (number, required) - Positive number (kg)
- `categoryId` (integer, required) - Valid category ID
- `specifications` (object, optional) - Key-value pairs
- `tags` (array, optional) - Array of strings

**Example Response:**
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "product": {
      "id": 2,
      "name": "Wireless Bluetooth Headphones",
      "slug": "wireless-bluetooth-headphones",
      "price": 599000,
      "sku": "WH-BLUETOOTH-001",
      "stock": 50,
      "isActive": true,
      "sellerId": 2,
      "createdAt": "2025-06-15T10:30:00Z"
    }
  }
}
```

## Update Product

**Permissions:** SELLER (own products), ADMIN, SUPER_ADMIN

**Endpoint:** `PUT /products/{id}`

**Request Body:**
```json
{
  "name": "Premium Wireless Bluetooth Headphones",
  "price": 549000,
  "stock": 75,
  "isActive": true
}
```

**Example Response:**
```json
{
  "success": true,
  "message": "Product updated successfully",
  "data": {
    "product": {
      "id": 2,
      "name": "Premium Wireless Bluetooth Headphones",
      "price": 549000,
      "stock": 75,
      "isActive": true,
      "updatedAt": "2025-06-15T10:35:00Z"
    }
  }
}
```

## Upload Product Images

**Permissions:** SELLER (own products), ADMIN, SUPER_ADMIN

**Endpoint:** `POST /products/{id}/images`

**Request Body:** `multipart/form-data`
- `images` (files, required) - Image files (JPG, PNG, WebP, max 5MB each, max 10 files)
- `primaryImageIndex` (integer, optional) - Index of primary image (default: 0)

**Example Response:**
```json
{
  "success": true,
  "message": "Product images uploaded successfully",
  "data": {
    "images": [
      {
        "id": 3,
        "url": "https://api.ecommerce-provider.com/uploads/products/headphones_1_main.jpg",
        "altText": "Wireless Bluetooth Headphones - Main View",
        "isPrimary": true
      },
      {
        "id": 4,
        "url": "https://api.ecommerce-provider.com/uploads/products/headphones_1_side.jpg",
        "altText": "Wireless Bluetooth Headphones - Side View",
        "isPrimary": false
      }
    ]
  }
}
```

## Delete Product

**Permissions:** SELLER (own products), ADMIN, SUPER_ADMIN

**Endpoint:** `DELETE /products/{id}`

**Example Response:**
```json
{
  "success": true,
  "message": "Product deleted successfully",
  "data": null
}
```

## Get Products by Seller

**Permissions:** SELLER (own products), ADMIN, SUPER_ADMIN

**Endpoint:** `GET /products/seller/{sellerId}`

**Path Parameters:**
- `sellerId` (integer, required) - Seller ID

**Query Parameters:**
- Same as `/products` endpoint

**Example Response:**
```json
{
  "success": true,
  "message": "Seller products retrieved successfully",
  "data": {
    "seller": {
      "id": 2,
      "firstName": "Jane",
      "lastName": "Smith",
      "shopName": "TechStore Indonesia"
    },
    "products": [
      // Product objects...
    ],
    "pagination": {
      // Pagination info...
    }
  }
}
```

---

## RBAC Permission Matrix

| Endpoint | SUPER_ADMIN | ADMIN | SELLER | CUSTOMER |
|----------|-------------|-------|--------|----------|
| `POST /auth/register` | ✅ | ✅ | ✅ | ✅ |
| `POST /auth/login` | ✅ | ✅ | ✅ | ✅ |
| `GET /users` | ✅ | ✅ | ❌ | ❌ |
| `POST /users` | ✅ (all roles) | ✅ (SELLER, CUSTOMER) | ❌ | ❌ |
| `PUT /users/{id}` | ✅ | ✅ | ✅ (own) | ✅ (own) |
| `PATCH /users/{id}/role` | ✅ (all roles) | ✅ (to SELLER) | ❌ | ❌ |
| `DELETE /users/{id}` | ✅ | ❌ | ❌ | ❌ |
| `GET /products` | ✅ | ✅ | ✅ | ✅ |
| `POST /products` | ✅ | ✅ | ✅ | ❌ |
| `PUT /products/{id}` | ✅ | ✅ | ✅ (own) | ❌ |
| `DELETE /products/{id}` | ✅ | ✅ | ✅ (own) | ❌ |
