# Используем официальный образ OpenJDK 17
FROM adoptopenjdk/openjdk17:alpine-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем pom.xml в контейнер
COPY pom.xml .

# Копируем исходный код приложения в контейнер
COPY src ./src

# Собираем приложение с помощью Maven
RUN ./mvnw package -DskipTests

# Запускаем приложение при старте контейнера
CMD ["java", "-jar", "target/joltartip.jar"]
