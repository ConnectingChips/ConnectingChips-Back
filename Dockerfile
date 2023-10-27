FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar

ENV TZ Asia/Seoul
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=dev", "/app.jar"]