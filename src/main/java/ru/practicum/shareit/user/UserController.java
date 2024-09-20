package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обработчик POST-запроса для создания нового пользователя.
     *
     * @param user объект, содержащий данные о пользователе.
     * @return DTO объект, содержащий данные о созданном пользователе.
     */
    @PostMapping
    public UserDto addUser(@Valid @RequestBody User user) {
        return UserDtoMapper.mapToUserDto(userService.addUser(user));
    }

    /**
     * Обработчик PATCH-запроса для обновления пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param user   объект, содержащий данные о пользователе.
     * @return DTO объект, содержащий данные об обновленном пользователе.
     */
    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @RequestBody User user, @PathVariable int userId) {
        return UserDtoMapper.mapToUserDto(userService.updateUser(userId, user));
    }

    /**
     * Обработчик GET-запроса для получения пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return DTO объект, содержащий данные о вещи.
     */
    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable int userId) {
        return UserDtoMapper.mapToUserDto(userService.getUser(userId));
    }

    /**
     * Обработчик DELETE-запроса для удаления пользователя.
     *
     * @param userId Идентификатор пользователя.
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

}
