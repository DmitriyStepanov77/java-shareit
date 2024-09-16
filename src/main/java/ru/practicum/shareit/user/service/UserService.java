package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    public UserDto addUser(User user);

    public UserDto updateUser(int id, User user);

    public UserDto getUser(int id);

    public void deleteUser(int id);
}
