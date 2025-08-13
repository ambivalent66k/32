#!/bin/bash
echo "🚀 Запуск системы хранения в продакшене..."

# Останавливаем и пересобираем
docker-compose -f docker-compose.prod.yml down
docker-compose -f docker-compose.prod.yml build --no-cache

# Запускаем все сервисы
docker-compose -f docker-compose.prod.yml up -d

echo "⏳ Ждем запуска всех сервисов..."
sleep 30

echo "📊 Статус сервисов:"
docker-compose -f docker-compose.prod.yml ps

echo ""
echo "✅ Система хранения запущена!"
echo "🌐 API доступно на: http://localhost:8080"
echo "📊 MinIO Console: http://localhost:9001"
echo "🗄️ PostgreSQL: localhost:5432"
