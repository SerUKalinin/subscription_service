package com.subscriptionservice.mapper;

import com.subscriptionservice.dto.UserDTO;
import com.subscriptionservice.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Маппер для преобразования между сущностью User и DTO.
 * Обеспечивает двустороннее преобразование с сохранением всех необходимых данных.
 */
@Component
public class UserMapper {
    
    /**
     * Преобразует DTO в сущность User.
     * Проверяет корректность входных данных.
     *
     * @param dto объект DTO для преобразования
     * @return сущность User
     * @throws IllegalArgumentException если dto равен null
     */
    public User toEntity(UserDTO dto) {
        Assert.notNull(dto, "UserDTO не может быть null");
        
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        return user;
    }
    
    /**
     * Преобразует сущность User в DTO.
     * Проверяет корректность входных данных.
     *
     * @param entity сущность User для преобразования
     * @return объект DTO
     * @throws IllegalArgumentException если entity равен null
     */
    public UserDTO toDTO(User entity) {
        Assert.notNull(entity, "User не может быть null");
        
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUserName(entity.getUserName());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    /**
     * Обновляет существующую сущность User данными из DTO.
     * Сохраняет существующие подписки пользователя.
     *
     * @param existingUser существующая сущность User
     * @param dto объект DTO с новыми данными
     * @return обновленная сущность User
     * @throws IllegalArgumentException если любой из параметров равен null
     */
    public User updateEntity(User existingUser, UserDTO dto) {
        Assert.notNull(existingUser, "Существующий User не может быть null");
        Assert.notNull(dto, "UserDTO не может быть null");
        
        existingUser.setUserName(dto.getUserName());
        existingUser.setEmail(dto.getEmail());
        return existingUser;
    }
} 