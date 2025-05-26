package com.subscriptionservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI для документации API.
 * Настраивает OpenAPI спецификацию с подробной информацией о сервисе,
 * включая контактные данные и лицензию.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Создает и настраивает OpenAPI спецификацию.
     * Определяет основную информацию о сервисе подписок, включая:
     * - Название и описание API
     * - Версию
     * - Контактную информацию
     * - Информацию о лицензии
     *
     * @return настроенный объект OpenAPI
     */
    @Bean
    public OpenAPI subscriptionServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Subscription Service API")
                        .description("API для управления пользователями и их подписками на сервисы")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Subscription Service Team")
                                .email("support@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
} 