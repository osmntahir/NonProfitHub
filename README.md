# Non-Profit News and Announcement Management System

## Introduction

The **Non-Profit News and Announcement Management System** is a web-based platform designed to facilitate the dynamic entry and management of News and Announcements for a non-profit organization's website. It provides separate admin interfaces for managing content and user interfaces for viewing news and announcements.

## Features

### Admin Panel
- **News Management:** Create, update, and delete news items.
- **Announcements Management:** Create, update, and delete announcements.

### User Interface
- **News Listing:** View a list of news articles with detailed pop-up views.
- **Announcements Listing:** View a list of announcements.

### Authentication
- Secure login and role-based access control through **Keycloak**.

## Technologies Used

- **Programming Language:** Java
- **Backend Framework:** Spring Boot
- **ORM:** Hibernate
- **Database:** PostgreSQL
- **Frontend Framework:** Vue
- **Authentication:** Keycloak
- **Containerization:** Docker, Docker Compose

## Getting Started

### Prerequisites

- **Docker** and **Docker Compose** installed on your machine.
- **Git** installed for version control.
- **Maven** installed.

### Setup

1. **Clone the Repository**

    ```bash
    git clone https://github.com/osmntahir/EventPortal.git
    cd EventPortal
    ```

2. **Configure Environment Variables**

    Ensure that the `.env` file is properly set up with the necessary environment variables. You can create a `.env` file in the root directory of the project based on the provided `.env.example` template.

3. **Build the Backend**

    Before running Docker Compose, build the backend application.

    ```bash
    cd context-manager
    mvn clean install -DskipTests
    cd ..
    ```

4. **Build and Run the Containers**

    Use Docker Compose to build and start all the services defined in the `docker-compose.yml` file.

    ```bash
    docker-compose up --build
    ```

    If you want to run the containers in the background (detached mode), use the following command:

    ```bash
    docker-compose up --build -d
    ```

    This command will:
    - Pull the necessary Docker images for PostgreSQL and Keycloak.
    - Build the `context-manager`, `admin-panel`, and `user-ui` services from their respective Dockerfiles.
    - Start all services and set up the network.

5. **Accessing the Application**

    Once all containers are up and running, you can access the different parts of the application using the following URLs:

    - **Admin Panel:** [http://localhost:3000](http://localhost:3000)
    - **User Interface:** [http://localhost:3001](http://localhost:3001)
    - **Keycloak Admin Console:** [http://localhost:9090](http://localhost:9090)
    - **Context Manager API:** [http://localhost:8080](http://localhost:8080)

6. **Initial Setup for Keycloak**

    The Keycloak server initializes by importing the configuration files located in the `keycloak-import` directory. These files are:

    - `cmv_realm-realm.json`
    - `cmv_realm-users-0.json`

    These configurations set up the necessary realms, clients, roles, and an admin user with the **Admin** role.

    - **Login Credentials:**
      - **Username:** `admin`
      - **Password:** `admin123`

    - **Accessing Keycloak Admin Console:**
      - Navigate to [http://localhost:9090](http://localhost:9090)
      - Log in using the admin credentials above.
      - From here, you can manage realms, clients, roles, and users as needed.

    - **Accessing Admin Panel:**
      - Use the same admin credentials (`admin` / `admin123`) to log into the Admin Panel at [http://localhost:3000](http://localhost:3000).

7. **Database Initialization**

    The PostgreSQL container initializes the database using the scripts located in the `./postgres-init` directory. Ensure that any necessary SQL scripts are placed in this directory for proper database setup.



## Usage

### Admin Panel

- **Accessing Admin Features:**
  - Navigate to [http://localhost:3000](http://localhost:3000).
  - Log in using the admin credentials:
    - **Username:** `admin`
    - **Password:** `admin123`

- **Managing News:**
  - Add new news items by providing the subject, content, validity date, and news link.
  - Edit or remove existing news items as needed.

- **Managing Announcements:**
  - Add new announcements by providing the subject, content, validity date, and uploading an image.
  - Edit or remove existing announcements as needed.

### User Interface

- **Viewing News:**
  - Navigate to [http://localhost:3001](http://localhost:3001).
  - Browse through the list of news articles.
  - Click on a news item to view detailed information in a popup.

- **Viewing Announcements:**
  - Browse through the list of announcements.
  - View announcement details, including images.

### Keycloak Administration

- **Accessing Keycloak Admin Console:**
  - Navigate to [http://localhost:9090](http://localhost:9090).
  - Log in using the admin credentials:
    - **Username:** `admin`
    - **Password:** `admin123`
  
- **Managing Realms, Clients, and Roles:**
  - From the admin console, you can create and manage realms, clients, and roles.
  - Assign roles to users as needed to control access within the application.

## Design Patterns

### Single Table Inheritance

The project employs the **Single Table Inheritance** design pattern for managing different types of events (e.g., `News` and `Announcement`) under a common base class (`Event`). In this pattern:

- **Base Class (`Event`)**: Both `News` and `Announcement` inherit common properties from `Event`.
- **Discriminator Column**: The `@DiscriminatorValue` annotation is used to store a type identifier (`NEWS` or `ANNOUNCEMENT`) in the database, which differentiates between the two types.
- **Inheritance**: This allows shared functionality to be handled in the base `Event` class, while `News` and `Announcement` entities can have additional fields (`newsLink` for `News` and `imagePath` for `Announcement`).

All event types are stored in a single database table, reducing complexity while allowing for flexibility in managing different types of events.


## Contact

For any inquiries or feedback, please contact [ozdemirosmantahir@gmail.com](mailto:ozdemirosmantahir@gmail.com).
