# Subscription Service

## Описание
Сервис управления подписками пользователей на цифровые сервисы. Позволяет управлять пользователями и их подписками на различные стриминговые платформы (YouTube Premium, VK Музыка, Яндекс.Плюс, Netflix и другие).

## Технологии
- Java 17
- Spring Boot 3.2.3
- PostgreSQL
- Docker
- Gradle 8.13
- Liquibase
- Swagger/OpenAPI 2.3.0
- SLF4J для логирования

## Требования
- Java 17 или выше
- Docker и Docker Compose
- PostgreSQL (если запуск без Docker)

## Установка и запуск

### Локальный запуск
1. Клонировать репозиторий:
```bash
git clone https://github.com/serukalinin/subscription_service.git
cd subscription_service
```

2. Настроить базу данных:
   - Создать базу данных PostgreSQL: `subscription_db`
   - Пользователь: `dev`
   - Пароль: `devpass`
   - Порт: `5432`

3. Запустить приложение:
```bash
./gradlew bootRun
```

### Запуск через Docker
1. Собрать и запустить контейнеры:
```bash
docker-compose up --build
```

## Доступ к API
- API доступно по адресу: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI документация: http://localhost:8080/api-docs

## API Endpoints

### Управление пользователями
- `POST /users` - создание пользователя
- `GET /users/{id}` - получение информации о пользователе
- `PUT /users/{id}` - обновление пользователя
- `DELETE /users/{id}` - удаление пользователя

### Управление подписками
- `POST /users/{id}/subscriptions` - добавление подписки
- `GET /users/{id}/subscriptions` - получение подписок пользователя
- `DELETE /users/{id}/subscriptions/{subscriptionId}` - удаление подписки
- `GET /subscriptions/top` - получение ТОП-3 популярных подписок

## Примеры запросов

### Создание пользователя
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "Иван Петров",
    "email": "ivan@example.com"
  }'
```

### Добавление подписки
```bash
curl -X POST http://localhost:8080/users/1/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "Netflix",
    "startDate": "2024-03-20T00:00:00",
    "endDate": "2024-04-20T00:00:00"
  }'
```

## Структура базы данных

### Таблица users
- `id` - уникальный идентификатор (BIGSERIAL)
- `user_name` - имя пользователя (VARCHAR)
- `email` - email пользователя (VARCHAR, UNIQUE)
- `created_at` - дата создания (TIMESTAMP)
- `updated_at` - дата обновления (TIMESTAMP)

### Таблица subscriptions
- `id` - уникальный идентификатор (BIGSERIAL)
- `service_name` - название сервиса (VARCHAR)
- `start_date` - дата начала подписки (TIMESTAMP)
- `end_date` - дата окончания подписки (TIMESTAMP)
- `user_id` - внешний ключ на таблицу users (BIGINT)

## Логирование
Логи приложения доступны в:
- Консоли
- Файле: `logs/application.log`
- Уровни логирования:
  - `root`: INFO
  - `com.subscriptionservice`: DEBUG
  - `org.springframework.web`: INFO
  - `org.hibernate.SQL`: DEBUG
  - `org.hibernate.type.descriptor.sql.BasicBinder`: TRACE
- Формат логов:
  - Консоль и файл: `%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n`
- Ротация логов:
  - Максимальный размер файла: 10MB
  - Максимальное количество файлов: 7

## Разработка
1. Форкните репозиторий
2. Создайте ветку для новой функциональности
3. Внесите изменения
4. Создайте pull request

## Тестирование
```bash
./gradlew test
```

## Лицензия
MIT
