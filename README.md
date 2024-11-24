# Contact Management System Backend

## Project Overview

This project is the backend for an Address Book Management System, built using Spring Boot. It handles user registration, login, address book management, and advanced features such as grouping, favorites, and data import/export. The backend communicates with the frontend through REST APIs, while the frontend is developed with Vue.js.

## Technology Stack
Spring Boot: Backend framework for efficient development.
MySQL: Database to store user and address book data.
Spring Security + JWT: For user authentication and role-based access control.

## Backend Directory Structure

.idea/                  # IDE configuration files
files/                  # Temporary files for import/export
src/
├── component/          # Shared components, e.g., WebSocket services
├── config/             # Configuration files (e.g., security, app settings)
├── controller/         # Controllers handling API requests
├── entity/             # Entity classes for database mapping
├── exception/          # Exception handling module
├── mapper/             # Data access layer (DAOs)
├── service/            # Service layer for business logic
├── utils/              # Utility classes for common functionalities
target/                 # Compiled files (not version-controlled)
pom.xml                 # Maven project configuration
address_springboot.iml  # IDE project configuration

## Features

1. User Module
Supports user registration and login.
Uses JWT (JSON Web Token) for secure authentication.
2. Address Book Management
CRUD operations for managing address book contacts.
Allows multiple contact details per entry, including:
Phone numbers
Emails
Social media handles
Physical addresses
3. Group Management
Allows users to create and edit custom groups such as:
Primary School Group, High School Group, University Group, Family Group, etc.
Contacts can be assigned to different groups for better organization.
4. Advanced Features
Favorite Contacts: Mark important contacts as favorites for easy access.
Import/Export:
Export: Download the address book data into an Excel file, with each contact in a row and attributes in separate columns.
Import: Bulk upload contacts from an Excel file into the address book.
