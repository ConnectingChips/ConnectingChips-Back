FROM openjdk:17
#ARG JAR_FILE=build/libs/*.jar
##COPY ${JAR_FILE} app.jar
COPY *.jar app.jar
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=dev", "/app.jar"]