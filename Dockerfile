# Этап сборки
FROM eclipse-temurin:17-jdk-alpine as build

# Установка необходимых инструментов
RUN apk add --no-cache gradle

# Установка рабочей директории
WORKDIR /app

# Копирование файлов проекта
COPY . .

# Сборка приложения
RUN gradle build --no-daemon

# Этап выполнения
FROM eclipse-temurin:17-jre-alpine

# Установка рабочей директории
WORKDIR /app

# Копирование собранного JAR-файла
COPY --from=build /app/build/libs/*.jar app.jar

# Создание непривилегированного пользователя
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Настройка переменных окружения
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Настройка healthcheck
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Запуск приложения
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 