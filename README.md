## About

This project is a simple RESTful API for managing users. It provides endpoints for creating, updating, deleting, and
retrieving users.

## Used Stack:

- **Development:** Java 17, Spring Boot 3.2.5
- **Build:** Maven
- **Testing:** JUnit 5, AssertJ, Mockito
- **Code Simplification and Logging:** Lombok, SLF4J with Logback
- **Code Quality:** Maven plugins (Checkstyle, PMD)
- **Test Coverage:** JaCoCo Maven Plugin (aiming for 80% coverage)
- **CI/CD:** GitHub Actions
- **Monitoring:** Spring Boot Actuator
- **Mapping:** MapStruct
- **Documentation:** Swagger, Javadoc

## How to run?

1) Clone repository

```shell
   git clone https://github.com/AndrewPleskanko/Clear_Solutions
```

2) Build project

```shell
   mvn clean install
```

3) Run project

```shell
   mvn spring-boot:run
```

4) Use VM options to set the minimum age for users

 ```copy
-DUSER_MIN_AGE=18
```

## How to test?

1) Open Swagger UI to test the endpoints

```copy
http://localhost:8080/swagger-ui.html
```

2) Use Postman collection to test the endpoints

```copy
Clear Solutions.postman_collection.json
```