# 🚀 KrisTechna Inventory Management System

A comprehensive Spring Boot web application for managing inventory at KrisTechna Tech Store. Built with Java 21, Spring Boot 3.5.7, and modern web technologies.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![H2 Database](https://img.shields.io/badge/H2-Database-blue)

## 📋 Project Overview

This application demonstrates full-stack development skills with:
- **Backend**: Spring Boot, JPA/Hibernate, REST APIs
- **Frontend**: Thymeleaf templates, responsive CSS
- **Database**: H2 with automatic sample data loading
- **Testing**: JUnit 5 unit tests
- **Validation**: Custom business logic and error handling

## 🛠️ Features

### Core Functionality
- ✅ **Parts Management** - Add, edit, delete Inhouse/Outsourced parts
- ✅ **Products Management** - Build products from multiple parts
- ✅ **Inventory Control** - Min/Max inventory validation
- ✅ **Multipack System** - Automatic handling of duplicate parts
- ✅ **Purchase System** - Buy Now functionality with stock management

### Advanced Features
- 🔍 **Search & Filter** - Real-time part and product search
- 💰 **Price Validation** - Products must cost ≥ sum of parts
- ⚠️ **Error Handling** - Comprehensive validation messages
- 📊 **Sample Data** - Auto-loaded tech store inventory
- 🧪 **Unit Tests** - Test coverage for critical business logic
src/
├── main/
│ ├── java/com/example/cs/
│ │ ├── controller/ # MVC Controllers
│ │ ├── model/ # JPA Entities
│ │ ├── repository/ # Spring Data JPA
│ │ ├── service/ # Business Logic
│ │ └── CsApplication.java
│ └── resources/
│ ├── templates/ # Thymeleaf Views
│ ├── static/ # CSS Styles
│ └── application.properties
└── test/
└── java/com/example/cs/
└── PartTest.java # Unit Tests

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.6+
- Git

### Installation
```bash
# Clone repository
git clone https://github.com/YOUR_USERNAME/kristech-inventory-system.git

# Navigate to project
cd kristech-inventory-system

# Run application
mvn spring-boot:run

# Access at http://localhost:8080

Default Access
Application: http://localhost:8080

H2 Console: http://localhost:8080/h2-console

Database URL: jdbc:h2:file:./data/kristinastechstore

Username: SA (no password)

📸 Application Screenshots
Home Dashboard
https://screenshots/home.png

Parts Management
https://screenshots/parts.png

Products with Parts Association
https://screenshots/products.png

🧪 Testing
Run the test suite:

bash
mvn test
Key test coverage:

Inventory validation (min/max bounds)

Multipack functionality

Business logic validation

🔧 Technology Stack
Backend: Spring Boot 3.5.7, Spring Data JPA, Spring MVC

Frontend: Thymeleaf, HTML5, CSS3, JavaScript

Database: H2 (embedded)

Build Tool: Maven

Java Version: 21

Testing: JUnit 5, Spring Boot Test

👨‍💻 Developer
    >>Hadik Hariyani

GitHub: https://github.com/Hardik86/kristech-inventory-system

LinkedIn: https://www.linkedin.com/in/hardik-hariyani/


📄 License
This project is part if portfolio and education purpose only.
## 🏗️ Architecture

