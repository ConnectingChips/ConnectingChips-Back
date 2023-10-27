FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar
ARG MAIN_PROFILE

ENV TZ Asia/Seoul
ENV PROFILE=${MAIN_PROFILE}
COPY ${JAR_FILE} app.jar



ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=$PROFILE", "/app.jar"]