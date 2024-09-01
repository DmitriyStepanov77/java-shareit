package ru.practicum.shareit.user.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeception.model.NotFoundExeception;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;

@Log4j2
@Component
public class UserInMemoryStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @Override
    public User addUser(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        log.info("Add user ID = {}, name = {}, email = {}.", user.getId(), user.getName(), user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("Update user ID = {}, name = {}, email = {}.", user.getId(), user.getName(), user.getEmail());
        return user;
    }

    @Override
    public User getUser(int id) {
        if (users.containsKey(id))
            return users.get(id);
        else
            throw new NotFoundExeception("Error: user is not found.");
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public void deleteUser(int id) {
        log.info("Delete user ID = {}.", id);
        users.remove(id);
    }

    private Integer getId() {
        return ++id;
    }
}
