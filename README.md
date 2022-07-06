### Simulating AWS S3 communication locally in Spring-boot using [s3ninja](https://github.com/scireum/s3ninja)

During local development, code involving communication with AWS S3 is difficult to simulate locally when security credentials are not present. This leads to Runtime exception of `401 Access Denied` and the code not being properly tested/run by developers. This Backend POC integrates S3 Ninja which emulates S3 communication and stores the file(s) locally in mounted file system. 

This can be helpful when IAM Role is being used or security credentials are being injected as environment variables in higher environments leaving no access to security credentials for development. Can also be integrated to be used for integration-testing.   

#### Alternatives to s3ninja 
* [Localstack](https://localstack.cloud)
* [Fake-s3](https://github.com/jubos/fake-s3)
* [s3Mock](https://github.com/adobe/S3Mock)

```
version: '3.7'
services:
  s3-ninja:
    image: scireum/s3-ninja:latest
    volumes:
      - ./data:/home/sirius/data
    ports:
    - 9000:9000
    networks:
      - s3-ninja-spring-boot-integration-network
      
  backend-service:
    build:
      context: ./
      dockerfile: Dockerfile
    ports:
    - 8080:8080
    depends_on:
      - s3-ninja
    environment:
      S3_NINJA_ENDPOINT: http://s3-ninja:9000
    networks:
      - s3-ninja-spring-boot-integration-network

networks:
  s3-ninja-spring-boot-integration-network:
```
---

### Pre-requisites
* Java 17 (recommended to use [SdkMan](https://sdkman.io))
```
sdk install java 17-open
```
* Maven (recommended to use [SdkMan](https://sdkman.io))
```
sdk install maven
```
* Docker

### Local Setup

* Clone the repo and run the below command in core

```
mvn clean install
```

* Run the below commands to build and run docker-compose

```
sudo docker-compose build
```
```
sudo docker-compose up -d
```
* The s3ninja UI can be accessed on the below address
```
http://localhost:9000/ui
```
* [Backend APIs](https://github.com/hardikSinghBehl/s3ninja-spring-boot-integration/blob/master/src/main/java/com/behl/emulator/controller/StorageController.java) can be accessed on the below base path
```
http://localhost:8080/storage
```

### Demonstration Screen-record

https://user-images.githubusercontent.com/69693621/177461979-a2b36b04-7931-49f0-8b10-a4a965901d45.mov

