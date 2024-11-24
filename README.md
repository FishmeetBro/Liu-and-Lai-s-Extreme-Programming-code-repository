# Contact Management System

This repository contains the source code for our **Contact Management System**, developed as part of the **Extreme Programming assignment** under the team name **"Liu and Lai"**. The system adopts a front-end and back-end separation architecture, with **Vue.js** for the front end and **Spring Boot** for the back end.

## Team Members

- **Liu Ruiyu**: Responsible for front-end development and design.
- **Lai Zhenpeng**: Responsible for back-end development and database management.

## Project Structure

The project is organized into three main branches:
- **`main`**: The primary branch containing the stable version of the code.
- **`front-head`**: Branch dedicated to front-end development using Vue.js.
- **`back-head`**: Branch dedicated to back-end development using Spring Boot.

## Features

### Front-end (Vue.js)
- User-friendly registration and login interfaces.
- Comprehensive contact management UI with filtering and search capabilities.
- Support for importing and exporting contacts in **CSV** and **Excel** formats.
- Ability to manage multiple contact methods for a single contact (e.g., phone, email, social media).
- Bookmarking feature for marking important contacts for quick access.

### Back-end (Spring Boot)
- Secure registration and login RESTful APIs.
- Full CRUD (Create, Read, Update, Delete) operations for contact management.
- APIs for importing and exporting contact data in standard formats.
- Support for storing and retrieving bookmarked contacts.
- Ability to handle multiple contact methods for each contact.

## Getting Started

### Prerequisites
Before running the project, ensure the following tools are installed:
- **Git**
- **Node.js** (for Vue.js development)
- **Vue CLI**
- **Java JDK** (version 8 or later, for Spring Boot)
- **Maven** (for building the back-end application)

### Installation Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/FishmeetBro/Liu-and-Lai-s-Extreme-Programming-code-repository.git
   ```

2. Navigate to the front-end project directory:
   ```bash
   cd Liu-and-Lai-s-Extreme-Programming-code-repository/front-end
   ```

3. Install front-end dependencies:
   ```bash
   npm install
   ```

4. Start the front-end development server:
   ```bash
   npm run serve
   ```

5. Navigate to the back-end project directory:
   ```bash
   cd ../back-end
   ```

6. Build and run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

7. Access the application:
   - Front-end: `http://localhost:8080`
   - Back-end API: `http://localhost:8081/api`

## Branch Workflow

### Development Process
- All development work should be done in the respective branches:
  - Front-end: `front-head`
  - Back-end: `back-head`
- Once a feature is complete and tested, merge it into the `main` branch.

### Contribution Workflow
1. Pull the latest code from the respective branch:
   ```bash
   git pull origin front-head  # For front-end
   git pull origin back-head   # For back-end
   ```

2. Make your changes and commit:
   ```bash
   git add .
   git commit -m "Describe your changes"
   ```

3. Push your changes:
   ```bash
   git push origin <branch-name>
   ```

4. Create a pull request on GitHub to merge changes into `main`.

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **Professor Lin, as well as to the teaching assistants Wu, Kong, and Liu** for guidance and feedback.
- The Extreme Programming methodology for inspiring our collaborative workflow.
