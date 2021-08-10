# HEYCAR CODING CHALLENGE

## Tech Stack
Java 15, Maven, Spring Boot, Spring Rest MVC implementation, MyBatis, Log4j2, Jackson JSON Spring implementation plus Google GSON for some internal task, OpenAPI, H2 In Memory DB, Google Guava and the usual Apache commons.
Java 15 is not a LTS but we moved to it in production for internal reasons, let's say a better transition from 11 LTS to 17 LTS.

## REST

* OpenAPI basic documentation (OpenApiConfig)
* REST implementation provided by Spring MVC.
* One only RestController with all the three operations. In a real production environment it should be split thanks to the business context.
* ControllerAdvisor as the exception manager, it inherits all the Spring defaults and manages as an example the ValidationException throw by the CSV parsing.

## DATABASE

* We use JPA via EclipseLink or MyBatis, for this solution I am using MyBatis integrated with a basic version of 
some components we use that deals with DB script versioning management.
* H2 in memory database (DatabaseConfig), SQL script in resources/database/1.0.0.0_SCHEMA.sql.
* Basic Datasource with no customization (max / min connection, validation query, etc).
* Database with no optimization except the PKs. 
Of course in production we would need unique indexes (CAR.ID_DEALER + CAR.CODE) plus indexed FK plus indexes on possibly all the fields used in the selection of cars.
* DEALER table is clearly dumb, I imagine in production it would have something more than a PK (we assume dealer id is an Integer).
* Data pagination is done in the simplest way using the offset / limit approach.
You can find all the possible approaches at this link that we consider the gold standard for our REST interfaces: https://opensource.zalando.com/restful-api-guidelines/#pagination
* I am using MyBatis just for a change (thanks to an excellent spring-mybatis bridge), I thought everybody else will be using JPA or Hibernate, this is just for showing that different approaches are possible.
All the SQL is logged in DEBUG thanks to <Logger name="com.heycar.model.mapper" level="DEBUG"/>.

## CSV Handling
* We usually adopt opencsv or commons-csv, this use case is extremely simple, I did everything in a few lines in
CarDTOMapper.getCarsFromCsv. Fail fast implementation, we throw a ValidationException at the first non parsable line (you have one of them in you PDF, a car with no color), 
which is an approach that is not always good depending on the quality of the input.

## Things that should be done in a production environment
* The number of cars managed in the REST listing endpoints should have a limit, otherwise the consumer could face timeout issues.
For this kind of issues, async processing could be a solution (or a limit checked in validation phase).
* Cars are upserted in a unique transaction, it could be a problem if we do not want a fail fast approach, it could be a problem if the number of inputs is too high (as a rule of thumb, transaction should not take too long / be too big).
* A Dealer set of endpoints is missing.
* Caching for listings.
* Matadata for listings (the generic information related to a car).
* Endpoints for SINGLE listing / car should be made available.
* We usually ship our MS with a properties config file + a log4j2 file, here the properties is missing and the configuration of log4j2 goes on console (something you usually do not want in production).
* A better Datasource configuration and Database object optimizations via FK and indexes.
* REST exception handling through Controller Advice is just an example of what we could do (check ValidationException management).
* OpenApi documentation is at the bare minimum (= no added annotation for the operation of the RestController).
* Immutability should be handled on the whole stack.
* I am truly sorry but I did not find that much time for the testing nor for dockerization.
The only test I provided is a Spring Boot setup / startup one (TestApplication) and an architecture one (TestArchitecture).
Of course I tested everything manually via Postman.
* Concurrency updates via versioning and optmistic locking is not taken in consideration.
* Case insensitive select / like select are missing, they require eventually a simple mapper.xml change.

## How to run the solution
* Simply start com.heycar.Application (DB in memory, port 8080 must be free).
* OpenApi here: http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/
