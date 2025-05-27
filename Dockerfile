FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9007
ENTRYPOINT ["java", "-jar" , "app.jar"]