#!/bin/bash
echo "🚀 Запуск системы хранения в режиме разработки..."

# Запускаем только базу данных и MinIO
docker-compose up -d postgres minio

echo "⏳ Ждем запуска сервисов..."
sleep 10

echo "✅ PostgreSQL доступен на порту 5432"
echo "✅ MinIO API доступен на порту 9000"
echo "✅ MinIO Console доступен на порту 9001"
echo ""
echo "Теперь можно запустить Spring Boot приложение:"
echo "./mvnw spring-boot:run"
