#!/bin/bash
echo "🛑 Остановка системы хранения..."

# Останавливаем dev окружение
docker-compose down

# Останавливаем prod окружение
docker-compose -f docker-compose.prod.yml down

echo "✅ Все сервисы остановлены"
