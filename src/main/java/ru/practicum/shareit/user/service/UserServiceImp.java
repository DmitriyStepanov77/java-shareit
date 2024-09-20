package ru.practicum.shareit.user.service;


import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserJpaRepository;

@Log4j2
@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UserJpaRepository userStorage;

    @Autowired
    public UserServiceImp(UserJpaRepository userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Добавление нового пользователя.
     *
     * @param user объект, содержащий данные о пользователе.
     * @return объект, содержащий данные о созданном пользователе.
     */
    @Override
    @Transactional
    public User addUser(User user) {
        validationUser(user);
        User userSaved = userStorage.save(user);
        log.info("Adding user: id = {}, name = {}.", userSaved.getId(), userSaved.getName());
        return userSaved;
    }

    /**
     * Обновление пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param user   объект, содержащий данные о пользователе.
     * @return объект, содержащий данные об обновленном пользователе.
     * @throws NotFoundException если пользователь не найден.
     */
    @Override
    @Transactional
    public User updateUser(int userId, User user) {
        User updateUser = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Error: user is not found."));
        if (user.getName() != null)
            updateUser.setName(user.getName());
        if (user.getEmail() != null)
            updateUser.setEmail(user.getEmail());
        validationUser(updateUser);
        log.info("Update user: userId = {}, name = {}.", user.getId(), user.getName());
        return userStorage.save(updateUser);
    }

    /**
     * Получение пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return объект, содержащий данные о пользователе.
     * @throws NotFoundException если пользователь не найден.
     */
    @Override
    public User getUser(int id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Error: user is not found."));
    }

    /**
     * Удаление пользователя.
     *
     * @param id Идентификатор пользователя.
     */
    @Override
    @Transactional
    public void deleteUser(int id) {
        log.info("Delete user: id = {}.", id);
        userStorage.deleteById(id);
    }

    private void validationUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new ValidationException("Email is empty.");
        if (userStorage.findAll().stream()
                .anyMatch(u -> (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId())))
            throw new ConflictException("Email already exists");
    }

}
