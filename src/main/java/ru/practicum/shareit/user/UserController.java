package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public UserDto addUser(@Valid @RequestBody User user) {
        return UserDtoMapper.mapToUserDto(userService.addUser(user));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@Valid @RequestBody User user, @PathVariable int id) {
        return UserDtoMapper.mapToUserDto(userService.updateUser(id, user));
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id) {
        return UserDtoMapper.mapToUserDto(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

}
