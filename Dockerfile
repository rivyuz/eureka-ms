FROM openjdk:8-jre-alpine

MAINTAINER Riviewz "contact@riviewz.com"

WORKDIR /usr/local/bin/

COPY ./target/eureka-ms-0.0.1-SNAPSHOT.jar eureka-ms.jar

CMD ["java", "-jar", "eureka-ms.jar"]