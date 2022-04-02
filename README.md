# KidZoo ToyStore Application

This is toy store application which provides REST endpoints for getting the available toys in the store. It has features to filter the toys based on the price ranges and also shows the status of the stock in the store.


## Endpoints available

1. http://localhost:8080/getToysList - GET Method - List of all toys

2. http://localhost:8080/getToysListByPriceRange - GET Method - List of all toys by price range

    RequestParameters:-
    MinimumPriceLimit - Integer,
    MaximumPriceLimit - Integer
3. http://localhost:8080/getToysListByStock - GET Method - List of all toys with the given status

   RequestParameters:-
   status - String
4. http://localhost:8080/getToyStatus/{id}

   PathParameter:-
   id - Integer


## Swagger - Api documentation

URL: http://localhost:8080/swagger-ui/index.html

## Maven Build command

mvn clean install

## Techologies used
 - Hibernate
 - Spring Boot
 - H2 database




 
