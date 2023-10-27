FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar
ENV PROFILE ${PROFILE}
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=${PROFILE}", "/app.jar"]