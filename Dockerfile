# FROM eclipse-temurin:17
# LABEL author=grasshopperplus
# COPY target/twitter-clone-0.0.1-SNAPSHOT.jar grasshopperback.jar
# EXPOSE 4000
# ENTRYPOINT [ "java", "-jar", "grasshopperback.jar" ]

# FROM openjdk:17-jdk-alpine
# COPY target/twitter-clone-0.0.1-SNAPSHOT.jar grasshopperback.jar
# ENTRYPOINT ["java", "-jar", "grasshopperback.jar"]

FROM openjdk:17
ARG JAR_FILE=target/*jar
COPY ${JAR_FILE} twitter-clone-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "twitter-clone-0.0.1-SNAPSHOT.jar"]