package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
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
     * @param userDto объект, содержащий данные о пользователе.
     * @return DTO объект, содержащий данные о созданном пользователе.
     */
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok()
                .body(UserDtoMapper.mapToUserDto(userService.addUser(UserDtoMapper.mapToUser(userDto))));
    }

    /**
     * Обработчик PATCH-запроса для обновления пользователя.
     *
     * @param userId  Идентификатор пользователя.
     * @param userDto объект, содержащий данные о пользователе.
     * @return DTO объект, содержащий данные об обновленном пользователе.
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int userId,
                                              @RequestBody UserDto userDto) {
        return ResponseEntity.ok()
                .body(UserDtoMapper.mapToUserDto(userService.updateUser(userId, UserDtoMapper.mapToUser(userDto))));
    }

    /**
     * Обработчик GET-запроса для получения пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return DTO объект, содержащий данные о вещи.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable int userId) {
        return ResponseEntity.ok()
                .body(UserDtoMapper.mapToUserDto(userService.getUser(userId)));
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
