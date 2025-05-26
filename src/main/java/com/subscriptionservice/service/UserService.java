package com.subscriptionservice.service;

import com.subscriptionservice.dto.UserDTO;
import com.subscriptionservice.exception.ResourceNotFoundException;
import com.subscriptionservice.exception.UserAlreadyExistsException;
import com.subscriptionservice.mapper.UserMapper;
import com.subscriptionservice.model.User;
import com.subscriptionservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для создания, получения, обновления и удаления пользователей.
 * Все операции выполняются в транзакционном контексте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Создает нового пользователя.
     * Проверяет уникальность email перед созданием.
     *
     * @param userDTO данные нового пользователя
     * @return созданный пользователь
     * @throws UserAlreadyExistsException если пользователь с таким email уже существует
     */
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.debug("Начало создания пользователя с email: {}", userDTO.getEmail());
        
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            log.warn("Попытка создания пользователя с существующим email: {}", userDTO.getEmail());
            throw new UserAlreadyExistsException("Пользователь с email " + userDTO.getEmail() + " уже существует");
        }

        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        log.info("Пользователь успешно создан с ID: {}", savedUser.getId());
        
        return userMapper.toDTO(savedUser);
    }

    /**
     * Получает пользователя по его ID.
     *
     * @param id идентификатор пользователя
     * @return информация о пользователе
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("Поиск пользователя по ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID {} не найден", id);
                    return new ResourceNotFoundException("Пользователь с ID " + id + " не найден");
                });
        
        log.debug("Пользователь с ID {} найден", id);
        return userMapper.toDTO(user);
    }

    /**
     * Обновляет информацию о пользователе.
     * Проверяет существование пользователя и уникальность нового email.
     *
     * @param id идентификатор пользователя
     * @param userDTO новые данные пользователя
     * @return обновленная информация о пользователе
     * @throws ResourceNotFoundException если пользователь не найден
     * @throws UserAlreadyExistsException если новый email уже используется
     */
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.debug("Начало обновления пользователя с ID: {}", id);
        
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь с ID {} не найден при попытке обновления", id);
                    return new ResourceNotFoundException("Пользователь с ID " + id + " не найден");
                });

        if (!existingUser.getEmail().equals(userDTO.getEmail()) && 
            userRepository.existsByEmail(userDTO.getEmail())) {
            log.warn("Попытка обновления email на уже существующий: {}", userDTO.getEmail());
            throw new UserAlreadyExistsException("Пользователь с email " + userDTO.getEmail() + " уже существует");
        }

        User updatedUser = userMapper.updateEntity(existingUser, userDTO);
        User savedUser = userRepository.save(updatedUser);
        log.info("Пользователь с ID {} успешно обновлен", id);
        
        return userMapper.toDTO(savedUser);
    }

    /**
     * Удаляет пользователя по его ID.
     * При удалении пользователя также удаляются все его подписки (каскадное удаление).
     *
     * @param id идентификатор пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional
    public void deleteUser(Long id) {
        log.debug("Начало удаления пользователя с ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            log.warn("Попытка удаления несуществующего пользователя с ID: {}", id);
            throw new ResourceNotFoundException("Пользователь с ID " + id + " не найден");
        }

        userRepository.deleteById(id);
        log.info("Пользователь с ID {} успешно удален", id);
    }

    /**
     * Получает сущность пользователя по его ID.
     *
     * @param id идентификатор пользователя
     * @return сущность пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional(readOnly = true)
    public User getUserEntityById(Long id) {
        log.info("Получение пользователя с ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", id);
                    return new ResourceNotFoundException("Пользователь не найден");
                });
    }
} 