# Multi-stage build для оптимизации размера
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Скачиваем зависимости (кешируется если pom.xml не изменился)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Копируем исходный код и собираем приложение
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Production образ
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Создаем пользователя для безопасности
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Устанавливаем curl для health check
RUN apk add --no-cache curl

# Копируем JAR файл из builder stage
COPY --from=builder /app/target/StorageSystem-*.jar app.jar

# Создаем папки для логов
RUN mkdir -p /app/logs && \
    chown -R appuser:appgroup /app

USER appuser

# Настройки JVM для контейнера
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
