FROM openjdk:17
EXPOSE 8080
COPY target/s3ninja-spring-boot-integration-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
LABEL maintainer="Hardik Singh Behl" email="behl.hardiksingh@gmail.com"