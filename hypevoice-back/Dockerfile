FROM openjdk:17
EXPOSE 8080
ARG JAR_FILE=build/libs/hypevoice-back-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} hypevoice-back-docker.jar
ENTRYPOINT ["java","-jar","/hypevoice-back-docker.jar"]