# Instructions

## How to start everything on docker
1. First start docker desktop [Download](https://www.docker.com/)
2. Run this command (first time) from root directory
```Docker compose up```
If you have made some changes in the code run this command
```docker compose up --build```
This is will apply the changes you made in docker

## Run things locally
1. Go to each module directory
Run this command
````mvn spring-boot:run -Dspring-boot.run.profiles=local````

## How to send REST api request (for frontend people)

There is a test page called [test.html](User-service/src/main/resources/static/test.html) scoll down and you will some react code how to send a request

IMPORTANT: I take no pride in test.html. Chatgpt have created the code(Alex)

Full directory: Frostbyte/User-service/src/main/resources/static/test.html

## What the ports the different modules run on
- User-service: 8000 (docker), 8001 (local)

## How to install maven

Go to this link: https://maven.apache.org/install.html

## Todo-list

#### Backend
- [X] Implemented user-service
- [X] Implemented passkeys
- [X] Implemented check-service
- [X] Implemented Gateway *(extra functionality)*
- [X] Implemented database for the user-service
- [X] Implemented database for the Check-service
- [X] Docker compose for whole project *(maybe only backend?)*
- [X] Delete user(child)/parent/employee *(extra functionality)*
- [X] Edit user(child)/parent/employee
- [X] register user(child)/parent/employee
- [X] Network for docker *(extra functionality)*
- [X] Add session token *(extra functionality)*
- [X] Healthchecks *(extra functionality)*

## Todo list frontend
- [ ] test


# How to login into the applicaiton

Username: user

Password: password