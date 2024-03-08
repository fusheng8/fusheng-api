FROM openjdk:17
ARG JAR_FILE
COPY $JAR_FILE /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]
