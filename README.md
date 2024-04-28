# Project Setup

## Prerequisites

Ensure you have the necessary software installed on your machine. This typically includes the Java Development Kit (JDK)
and a Java IDE such as IntelliJ IDEA or Eclipse.

## Clone the Repository

Clone the project repository from the source control tool (like GitHub, Bitbucket, etc.).

## Import the Project

Open your Java IDE and import the cloned project. Usually, you can do this by selecting File -> Open and navigate to the
project directory.

## Configure VM Options

In your IDE, locate the configuration settings for the project. This is usually found in the Run/Debug Configurations
section. In the VM options field, enter the following:

```
-Duser.min.age=18
```

## Testing the Project with Postman

#### To test the project using the provided Postman collection, follow these steps:

Download the Collection: Download the Postman collection from the project repository. The collection file in the root
directory of the project.
Import the Collection: Open Postman, click on File -> Import... and navigate to the location where you saved the
downloaded Postman collection file.
Run the Requests: Once the collection is imported, you can run the requests to test the different endpoints of the
application. Make sure your application is running when you send the requests.

## Run the Project

Now, you should be able to run the project. Use the Run command in your IDE to start the application.

## Used Stack:

- Development: Java 17, Maven
- Testing: JUnit 5, AssertJ, Mockito
- Code Simplification and Logging: Project Lombok, SLF4J with Logback
- Code Quality Management: Maven plugins (Checkstyle, PMD)
- Test Coverage: JaCoCo Maven Plugin (aiming for 80% coverage)
- CI/CD: GitHub Actions
- Monitoring: Spring Boot Actuator
- Mapping: MapStruct
- Documentation: Swagger, Javadoc
