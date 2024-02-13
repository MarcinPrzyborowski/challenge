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

### Using Docker

1. Package the application with Maven:
   1. Ensure you are in the project root directory and run:
   2. mvn clean package
2. Build the Docker image:
   1. After packaging, build the Docker image with:
   2. ``docker build -t git-repo-fetcher .``
3. Run the application in a Docker container:
   1. Start the container using:
   2. ``docker run -p 8080:8080 git-repo-fetcher``
4. The application will be accessible at http://localhost:8080 and the Swagger UI can be accessed at http://localhost:8080/swagger-ui.html.


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

## API Documentation

The OpenAPI documentation for this project is available in the `api-docs.yaml` file located at:

```
docs/api-docs.yaml
```
Additionally, you can view and interact with the API's endpoints through the Swagger UI, which is accessible at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

This web interface provides a detailed look at all available RESTful endpoints, allowing you to test them directly from your browser. It's an invaluable tool for both development and testing phases.