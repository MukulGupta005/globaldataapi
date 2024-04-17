# Use Maven to build the application
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Use OpenJDK for the final stage
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/countrydata-0.0.1-SNAPSHOT.jar countrydata.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","countrydata.jar"]
