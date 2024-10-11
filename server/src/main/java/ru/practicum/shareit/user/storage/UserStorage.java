package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    public User addUser(User user);

    public User updateUser(User user);

    public User getUser(int id);

    public Collection<User> getUsers();

    public void deleteUser(int id);
}
