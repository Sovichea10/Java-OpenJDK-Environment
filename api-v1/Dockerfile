# Stage 1
FROM openjdk:17-jdk-slim AS build

RUN apt-get update -qq && \
    apt-get install -yqq curl unzip;

COPY . /opt/pos
WORKDIR /opt/pos

# Build using Gradle
RUN ./gradlew build

# # Stage 2
FROM openjdk:17-jdk-slim

COPY --from=build /opt/pos/build/libs/pos.jar pos.jar

EXPOSE 8081

CMD java -jar pos.jar
