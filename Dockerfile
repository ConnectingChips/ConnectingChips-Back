FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar
ARG PROFILE

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=${PROFILE}", "/app.jar"]