# angular-boot

This app is a combination of Angular as a frontend and Spring Boot as a backend; it should serve as an intranet system for multiple users. The backend provides the information via APIs (i.a. out of a MySQL database). The frontend calls these APIs.

The backend provides two CRUD APIs for facilities (Facility) and their contacts (FacilityContact). Next step would be implementing all API functions in the frontend and ensuring an authorization of both the frontend and the APIs of the backend. 

So far, the technologies I used:
* Spring Boot
* JUnit 5
* Mockito
* Spring Data (with MySQL and Hibernate, H2 for testing)
* Lombok
* Angular 6
