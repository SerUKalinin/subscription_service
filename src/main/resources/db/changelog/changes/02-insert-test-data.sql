
-- Добавление тестовых пользователей
INSERT INTO users (user_name, email, created_at, updated_at)
VALUES 
    ('Иван Петров', 'ivan@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Анна Смирнова', 'anna@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Алексей Иванов', 'alexey@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Добавление тестовых подписок
INSERT INTO subscriptions (service_name, start_date, end_date, user_id)
VALUES 
    ('Netflix', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 1),
    ('YouTube Premium', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 1),
    ('VK Музыка', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 1),
    ('Spotify', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 1),
    ('Apple Music', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 2),
    ('Disney+', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 2),
    ('HBO Max', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 2),
    ('Яндекс.Плюс', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 2),
    ('Netflix', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 3),
    ('Spotify', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 3),
    ('Disney+', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month', 3); 