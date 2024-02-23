FROM maven:3.6.3-openjdk-17 AS build
WORKDIR /build
COPY src ./src
COPY pom.xml ./
RUN mvn clean package -e -DskipTests


FROM openjdk:17-jdk AS app
WORKDIR /app
COPY --from=build /build/target/jortartip-0.0.1-SNAPSHOT*jar joltartip.jar
CMD ["java", "-jar", "joltartip.jar"]
