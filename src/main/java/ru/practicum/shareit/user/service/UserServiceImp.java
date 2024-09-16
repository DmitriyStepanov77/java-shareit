package ru.practicum.shareit.user.service;


import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Log4j2
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserStorage userStorage;

    @Override
    public UserDto addUser(User user) {
        validationUser(user);
        return UserDtoMapper.mapToUserDto(userStorage.addUser(user));
    }

    @Override
    public UserDto updateUser(int id, User user) {
        User updateUser = userStorage.getUser(id);
        if (user.getName() != null)
            updateUser.setName(user.getName());
        if (user.getEmail() != null)
            updateUser.setEmail(user.getEmail());
        validationUser(updateUser);
        return UserDtoMapper.mapToUserDto(userStorage.updateUser(updateUser));
    }

    @Override
    public UserDto getUser(int id) {
        return UserDtoMapper.mapToUserDto(userStorage.getUser(id));
    }

    @Override
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    private void validationUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new ValidationException("Email is empty.");
        if (userStorage.getUsers().stream()
                .anyMatch(u -> (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId())))
            throw new ConflictException("Email already exists");
    }

}
