FROM eclipse-temurin:17-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} puntos-1.0.jar
ENTRYPOINT ["java","-jar","/puntos-1.0.jar"]