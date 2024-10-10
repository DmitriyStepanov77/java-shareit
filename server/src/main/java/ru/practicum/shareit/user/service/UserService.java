package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

public interface UserService {
    public User addUser(User user);

    public User updateUser(int id, User user);

    public User getUser(int id);

    public void deleteUser(int id);
}
