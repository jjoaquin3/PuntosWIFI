FROM eclipse-temurin:17.0.6_10-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} puntos-1.0.0.jar
ENTRYPOINT ["java","-jar","/puntos-1.0.0.jar"]