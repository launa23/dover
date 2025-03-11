# Step 1: Build app
# Start with Maven image that includes JDK 21
FROM maven:3.9-amazoncorretto-21 AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build src with Maven
RUN mvn clean package -Pprod -DskipTests

#---------------------------------------------------------------------
# Step 2: Create image
FROM amazoncorretto:21.0.6

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]