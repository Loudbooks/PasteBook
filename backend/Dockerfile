
FROM openjdk:23-slim

WORKDIR /pastebook-backend

COPY build/libs/pastebook.jar pastebook.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "pastebook.jar"]