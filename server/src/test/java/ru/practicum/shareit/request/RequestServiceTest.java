package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class RequestServiceTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestJpaRepository itemRequestStorage;

    @Mock
    private UserServiceImp userService;

    private User user;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    void setup() {
        LocalDateTime currentTime = LocalDateTime.now();

        user = new User();
        user.setId(1);
        user.setName("Jhon");
        user.setEmail("jhon@mail.ru");

        item = new Item();
        item.setId(1);
        item.setDescription("Item 1 desc.");
        item.setName("Item 1");
        item.setAvailable(true);
        item.setOwner(user);

        itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setRequester(user);
        itemRequest.setCreated(currentTime);
        itemRequest.setDescription("Give me item");
    }

    @Test
    void addItemRequestTest() {
        Mockito
                .when(itemRequestStorage.save(any()))
                .thenReturn(itemRequest);

        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);

        ItemRequest itemRequestSaved = itemRequestService.addRequest(1, itemRequest);
        Mockito.verify(itemRequestStorage).save(any());

        assertEquals(itemRequest.getId(), itemRequestSaved.getId());

        verify(itemRequestStorage, atMostOnce()).save(any());
    }

    @Test
    void getItemRequestTest() {
        Mockito
                .when(itemRequestStorage.findById(any()))
                .thenReturn(Optional.ofNullable(itemRequest));

        ItemRequest itemRequestGet = itemRequestService.getRequest(1);
        Mockito.verify(itemRequestStorage).findById(any());

        assertEquals(itemRequest.getId(), itemRequestGet.getId());

        verify(itemRequestStorage, atMostOnce()).findById(any());
    }

    @Test
    void getAllItemRequestTest() {
        Mockito
                .when(itemRequestStorage.findAll())
                .thenReturn(List.of(itemRequest));

        List<ItemRequest> itemRequestsGet = itemRequestService.getAllRequest();
        Mockito.verify(itemRequestStorage).findAll();

        assertEquals(itemRequestsGet.size(), 1);

        verify(itemRequestStorage, atMostOnce()).findAll();
    }

    @Test
    void getAllItemRequestByRequesterTest() {
        Mockito
                .when(itemRequestStorage.findByRequesterId(anyInt()))
                .thenReturn(List.of(itemRequest));
        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);

        List<ItemRequest> itemRequestsGet = itemRequestService.getAllRequestByRequester(1);
        Mockito.verify(itemRequestStorage).findByRequesterId(anyInt());

        assertEquals(itemRequestsGet.size(), 1);

        verify(itemRequestStorage, atMostOnce()).findByRequesterId(anyInt());
    }

}