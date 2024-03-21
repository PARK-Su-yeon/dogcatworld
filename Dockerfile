FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} abandoneddog-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/abandoneddog-0.0.1-SNAPSHOT.jar"]