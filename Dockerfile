FROM eclipse-temurin:17

LABEL author=grasshopperplus

COPY target/twitter-clone-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]

# FROM alpine:latest
# RUN apk add openjdk17

# RUN apk update
# RUN apk upgrade --available && sync 
# RUN mkdir -p /app
# COPY target/*jar /app/.
# CMD ["/bin/sh", "-c", "java -jar /app/*jar"]