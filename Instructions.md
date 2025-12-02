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

## Create a test user
Run this command
```` curl -X POST http://localhost:8000/api/employees \
  -H "Content-Type: application/json" \
  -d '{
        "name": "bob",
        "email": "bob@example.com",
        "password": "secret123",
        "phoneNumber": 12345678,
        "address": "Somewhere",
        "profilePictureUrl": null
      }'
      
   ````


## Todo-list

#### Backend
- [ ] Implemented user-service (Missing departments)
- [X] Implemented passkeys
- [ ] Implemented check-service
- [ ] Implemented Gateway
- [ ] Added frontend to gateway
- [ ] Implemented ActivityPlan (extra functionality)
- [X] Implemented database for the user-service
- [ ] Implemented database for the Check-service
- [ ] Scaling and loadbalance (extra functionality)
- [ ] Consul (extra functionality)
- [ ] Docker compose for whole project
- [ ] Delete user(child)/parent/employee
- [ ] Edit user(child)/parent/employee
- [ ] register user(child)/parent/employee
- [ ] Network for docker

