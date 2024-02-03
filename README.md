# Product & Category Management Microservice

## Description
This project demonstrates a Java Spring Boot microservice designed to manage products and categories (core models).

It follows the principles of Hexagonal and Onion Architecture.
Apache Maven is used to manage the project. Docker is used to build and run the whole service as one unit.

There are role based RestControllers.
The communication is based from Interface to Implementation and so on and so forth.

![Hexagonal Architecture in combination](img/architecture.png)

## Tests

Unit tests are written for Domain Services.
Integration tests are written for ApplicationService/s.
Integration tests are written for role based RestControllers.
