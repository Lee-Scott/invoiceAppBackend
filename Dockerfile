# Start With: ------------------------
# SECRET=NEWSECRETHERE ./start-dev.sh
#------------------------------------------
# Step 1: Build the application
# We are using a Maven base image for building our application
FROM maven:3.9.6 AS build

# Set the working directory in the Docker image
WORKDIR /app

# Define an argument for the container port
ARG CONTAINER_PORT

# Copy the pom.xml file to the Docker image
COPY pom.xml /app

# Resolve all dependencies of the project
RUN mvn dependency:resolve

# This is a good practice because you can break down your Dockerfile into steps for performance
# Copy the rest of the project to the Docker image
COPY . /app

# Clean the project (remove target directory, etc.)
RUN mvn clean

# -X for more logs
# Build the project and skip tests
RUN mvn package -DskipTests -X

# Step 2: Run the application
# We are using an OpenJDK base image to run our application
FROM openjdk:20

# We can define the jar file in a pom
# Copy the built jar file from the build stage to this new stage
COPY --from=build /app/target/*.jar app.jar

# Expose the container port
EXPOSE ${CONTAINER_PORT}

# Command to run the application
CMD ["java", "-jar", "app.jar"]

# Some commonly used Docker commands:
# Build a Docker image: docker build -t my-image .
# Run a Docker container: docker run -p 8080:8080 my-image
# Check running Docker containers: docker ps
# Check all Docker containers: docker ps -a
# Stop a Docker container: docker stop <container-id>
# Remove a Docker container: docker rm <container-id>
# Check Docker images: docker images
# Remove a Docker image: docker rmi <image-id>
# Get logs of a Docker container: docker logs <container-id>
# Show whats on port 8000: docker ps | grep 8000