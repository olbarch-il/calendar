# Event Management API

## Objective
This project aims to develop a RESTful API for managing events, enabling users to schedule, retrieve, update, delete, and receive reminders for events, along with offering advanced features.

## Architecture
The API is built using the Spring Boot framework, chosen for its robustness and ease of use. While a microservice architecture could be considered for scaling, this project adopts a monolithic approach for simplicity and ease of management.

### Components
- **Events**: Core functionalities related to event management.
  - **Controller**: Handles HTTP requests.
    - `EventController`: Manages individual events.
    - `BatchEventController`: Manages batch operations on events.
  - **DAO**: Data Access Objects for database interactions.
    - `EventRepository`: Accesses the event database.
  - **Model**: Defines the data structure.
    - `dto`: Data Transfer Objects.
    - `entity`: JPA Entities for ORM.
    - `exception`: Custom exceptions.
  - **Service**: Business logic.
    - `EventService`: Event management logic.
    - `BatchEventService`: Batch event operations.
    - `EventSpecifications`: Specifications for queries.
- **Notifications**: Handles event notifications.
  - **Model**: Notification data models.
  - **Service**: Processes and sends notifications.
- **Subscription**: Manages event subscriptions.

## Features
- **API Design & Setup**: The API supports creating, retrieving, updating, deleting, and reminding of events. Basic authentication is implemented for security.
- **Database Integration & Advanced Querying**: Utilizes a relational database for storing event details, with support for advanced querying based on location, venue, date, and popularity.
- **Event Reminders**: Sends reminders 30 minutes before the scheduled event time.
- **Documentation & Testing**: API documentation is available via Swagger UI, and the project includes both unit and integration tests for reliability.
- **Bonus Features**: Supports batch operations, rate limiting, event subscriptions, and real-time notifications.

## Setup and Launch
Detail the setup and launch process, including prerequisites like Java installation, database configuration, etc.

## API Documentation
Access the Swagger UI for detailed API documentation and testing at: http://localhost:8080/swagger-ui/index.html

## Setup and Launch

### Prerequisites
- Java 11 or newer
- Maven (for building the project)
- An IDE of your choice (e.g., IntelliJ IDEA, Eclipse)

### Database Configuration
1. you can use in-memory h2 with the configurations in application.yml

### Running the Application
1. Clone the repository from GitHub: git clone [repository link]
2. Navigate to the project directory: mvn clean install
3. Build the project using Maven: java -jar target/[generated-jar-file].jar

Alternatively, if you're using an IDE, you can run the application by executing the main class `com.targil.calendar.CalendarApplication`.

### Accessing the API
Once the application is running, you can access the API endpoints through any HTTP client (e.g., Postman) or by visiting the Swagger UI at http://localhost:8080/swagger-ui/index.html for interactive documentation and testing.

### Basic Authentication
The API uses basic authentication. To access protected endpoints, you will need to provide a valid username and password. You can register a new user via the `/api/users/register` endpoint or use predefined credentials if available.

### Swagger UI
The Swagger UI provides an interactive way to explore and test the API endpoints. You can access it at http://localhost:8080/swagger-ui/index.html. Note that you may need to authenticate to access certain endpoints.

### Logging
Logs are written to the console and a file in the `/logs` directory. You can configure logging levels and destinations in `src/main/resources/application.properties`.

This setup guide should help you get the application running on your local machine for development and testing purposes.

