FROM openjdk:17-jdk-slim
# Netcat alətini quraşdırın
RUN apt-get update && apt-get install -y netcat

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle build output JAR file into the container
COPY build/libs/ExpenseIncomeProject-0.0.1-SNAPSHOT.jar /app/ExpenseIncomeProject.jar

# Expose the port the app will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "ExpenseIncomeProject.jar"]
