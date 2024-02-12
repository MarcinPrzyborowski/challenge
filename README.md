# Git Repository Data Fetcher

This Spring Boot application is designed to fetch and display data about repositories from Git. It allows users to input a repository's name and then displays various information such as the latest commits, contributors, and licensing information.

## Requirements

- Java 17
- Maven

## Setup

To get started with this project after cloning the repository, follow these steps.
1. Ensure you have Java Development Kit (JDK) 17 installed on your machine.

## Running the Application

You can run this project in several ways:

### Using an IDE

1. Open the project in your preferred IDE that supports Maven, such as IntelliJ IDEA, Eclipse, or VSCode.
2. Locate the `TaskApplication.java` file that contains the `main` method.
3. Run the application using the IDE's built-in tools.

### Using Maven Wrapper

For Unix/Linux systems:

```bash
./mvnw spring-boot:run
```

For Windows systems:
```cmd
mvnw.cmd spring-boot:run
```

## Testing

This project uses JUnit and Mockito for unit testing and integration testing. To run the tests, follow these steps:

1. Navigate to the project root directory in your terminal or command prompt.
2. Execute the following command to run all tests:

```bash
mvnw test
```

This command will run all unit and integration tests in the project and provide a summary of the test results.

