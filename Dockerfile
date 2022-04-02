FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*SNAPSHOT.jar

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar", "-Xmx4g"]