# Java API Test Automation

[![Build Status](https://github.com/felseje/java-api-test-automation-framework/actions/workflows/maven-ci.yml/badge.svg)](https://github.com/felseje/java-api-test-automation-framework/actions)

## Project Description

**Java API Test Automation** is an automated API testing project built with **Java**. It uses **JUnit Jupiter** for test execution and **RestAssured** for HTTP requests and response validation.

The project demonstrates a structured approach to API test automation, with reusable components for request configuration, response handling, assertions, logging, and test support.

The tests are implemented against the [**ServeRest API**](https://serverest.dev/), providing a practical example of API test automation using a real REST API.

---

## Prerequisites

Before running the project, make sure you have installed:

- [Java JDK 25](https://openjdk.org/projects/jdk/25/);
- [Maven 3.9+](https://maven.apache.org/download.cgi);
- *Your preferred IDE* (**IntelliJ IDEA**, **Eclipse**, **VS Code** etc.).

## Installation

1. Clone the repository:

```bash
git clone https://github.com/felseje/java-api-test-automation.git
cd java-api-test-automation
```

2. Install the Maven dependencies:

```bash
mvn clean install
```

## Running Tests

The API tests are implemented as integration tests and follow the *IT naming convention. They are executed by the [**Maven Failsafe Plugin**](https://maven.apache.org/surefire/maven-failsafe-plugin/).

To run the tests:

```bash
mvn verify
```

Failsafe test reports are generated in:

target/failsafe-reports

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── io/github/felseje/apitestautomation/
│   │       ├── client/          # API clients and DTOs
│   │       ├── config/          # Application and test configuration
│   │       ├── core/            # Core HTTP and test support components
│   │       ├── exception/       # Custom exceptions
│   │       ├── factory/         # Request and response specification factories
│   │       ├── service/         # Service-level operations
│   │       └── util/            # Shared utility classes
│   └── resources/
│       └── config/
└── test/
    ├── java/
    │   └── io/github/felseje/apitestautomation/
    │       ├── suite/            # Test suites
    │       ├── test/             # API integration tests (*IT)
    │       └── testdata/         # Test data
    └── resources/
        └── schemas/              # JSON schemas for response validation
            ├── login/
            └── user/
```

## Example Tests

The project contains automated integration tests against the ServeRest API, covering different API behaviors and demonstrating how the test automation components can be used in practice.

## Technologies Used

- [Java 25](https://openjdk.org/projects/jdk/25/) – Modern, high-performance JDK
- [Maven](https://maven.apache.org/) – Build automation and dependency management
- [JUnit Jupiter 6](https://junit.org/junit5/docs/current/user-guide/) – Unit and integration testing framework
- [RestAssured 6](https://rest-assured.io/) – Simplified REST API testing
- [Awaitility 4.3](https://awaitility.github.io/awaitility/) – Handling asynchronous operations in tests
- [SLF4J](http://www.slf4j.org/) + [Logback](https://logback.qos.ch/) – Logging framework
- [Lombok](https://projectlombok.org/) – Boilerplate code reduction
- [Owner](https://github.com/lviggiano/owner) – Type-safe configuration management

## License

This project is licensed under the **MIT License**. See the **LICENSE** file for details.