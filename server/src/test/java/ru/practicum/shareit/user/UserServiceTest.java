package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImp;
import ru.practicum.shareit.user.storage.UserJpaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class UserServiceTest {

    @InjectMocks
    private UserServiceImp userService;

    @Mock
    private UserJpaRepository userStorage;

    private User user1;

    private User user2;

    @BeforeEach
    void setup() {
        user1 = new User();
        user1.setId(1);
        user1.setName("Jhon");
        user1.setEmail("jhon@mail.ru");

        user2 = new User();
        user2.setId(1);
        user2.setName("Jhon2");
        user2.setEmail("jhon@mail.ru");
    }

    @Test
    void addUserTest() {
        Mockito
                .when(userStorage.save(any()))
                .thenReturn(user1);

        User userSaved = userService.addUser(user1);
        Mockito.verify(userStorage).save(any());

        assertEquals(user1.getId(), userSaved.getId());

        verify(userStorage, atMostOnce()).save(any());
    }

    @Test
    void addUserFailTest() {
        Mockito
                .when(userStorage.findAll())
                .thenReturn(List.of(user1));
        user2.setId(2);

        assertThrows(ConflictException.class, () -> userService.addUser(user2));

        verify(userStorage, atMostOnce()).findAll();
    }

    @Test
    void getUserTest() {
        Mockito
                .when(userStorage.findById(any()))
                .thenReturn(Optional.ofNullable(user1));

        User userGet = userService.getUser(1);
        Mockito.verify(userStorage).findById(any());

        assertEquals(user1.getId(), userGet.getId());

        verify(userStorage, atMostOnce()).findById(any());
    }

    @Test
    void updateUserTest() {
        Mockito
                .when(userStorage.save(any()))
                .thenReturn(user1);

        Mockito
                .when(userStorage.findById(any()))
                .thenReturn(Optional.ofNullable(user1));

        User userGet = userService.updateUser(1, user2);
        Mockito.verify(userStorage).save(any());

        assertEquals(user2.getName(), userGet.getName());

        verify(userStorage, times(1)).save(any());
    }

    @Test
    void deleteUserTest() {
        Integer userId = user1.getId();

        userService.deleteUser(userId);

        verify(userStorage).deleteById(userId);
    }
}