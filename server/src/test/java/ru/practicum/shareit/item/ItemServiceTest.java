package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingJpaRepository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImp;
import ru.practicum.shareit.item.storage.CommentJpaRepository;
import ru.practicum.shareit.item.storage.ItemJpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class ItemServiceTest {

    @InjectMocks
    private ItemServiceImp itemService;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemJpaRepository itemStorage;

    @Mock
    private UserServiceImp userService;

    @Mock
    private BookingJpaRepository bookingStorage;

    @Mock
    private ItemRequestJpaRepository itemRequestStorage;

    @Mock
    private CommentJpaRepository commentStorage;

    private User user;
    private Item item1;
    private Item item2;

    private Booking booking;

    private Comment comment;

    private CommentDto commentDto;

    private ItemDto itemDto;

    private ItemDtoOut itemDtoOut;

    private ItemRequest itemRequest;

    @BeforeEach
    void setup() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        user = new User();
        user.setId(1);
        user.setName("Jhon");
        user.setEmail("jhon@mail.ru");

        item1 = new Item();
        item1.setId(1);
        item1.setDescription("Item 1 desc.");
        item1.setName("Item 1");
        item1.setAvailable(true);
        item1.setOwner(user);

        item2 = new Item();
        item2.setId(1);
        item2.setDescription("Item 2 desc.");
        item2.setName("Item 2");
        item2.setAvailable(true);
        item2.setOwner(user);

        itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setItems(List.of(item1));
        itemRequest.setRequester(user);
        itemRequest.setDescription("Give me item");

        booking = new Booking();
        booking.setId(1);
        booking.setItem(item1);
        booking.setBooker(user);
        booking.setStart(start);
        booking.setEnd(end);

        comment = new Comment();
        comment.setId(1);
        comment.setText("Good");
        comment.setItem(item1);
        comment.setAuthor(user);

        commentDto = new CommentDto();
        commentDto.setText("Good");
    }

    @Test
    void addItemTest() {
        itemDto = ItemDtoMapper.mapToItemDto(item1);

        Mockito
                .when(itemStorage.save(any()))
                .thenReturn(item1);
        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);

        Item itemSaved = itemService.addItem(1, itemDto);
        Mockito.verify(itemStorage).save(any());

        assertEquals(itemDto.getId(), itemSaved.getId());

        verify(itemStorage, atMostOnce()).save(any());
    }

    @Test
    void getItemTest() {
        Mockito
                .when(itemStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(item1));
        Mockito
                .when(commentStorage.findByItemId(anyInt()))
                .thenReturn(List.of(comment));
        Mockito
                .when(bookingStorage.findByItemId(anyInt()))
                .thenReturn(List.of(booking));

        itemDtoOut = itemService.getItem(1);
        Mockito.verify(itemStorage).findById(anyInt());

        assertEquals(itemDtoOut.getId(), item1.getId());

        verify(itemStorage, atMostOnce()).findById(anyInt());
    }

    @Test
    void getAllItemTest() {
        Mockito
                .when(itemStorage.findByOwnerId(anyInt()))
                .thenReturn(List.of(item1));
        Mockito
                .when(commentStorage.findByItemId(anyInt()))
                .thenReturn(List.of(comment));
        Mockito
                .when(bookingStorage.findByItemIdIn(anyList()))
                .thenReturn(List.of(booking));

        List<ItemDtoOut> itemsGet = itemService.getItems(1);
        Mockito.verify(itemStorage).findByOwnerId(anyInt());

        assertEquals(itemsGet.size(), 1);

        verify(itemStorage, atMostOnce()).findByOwnerId(anyInt());
    }

    @Test
    void updateItemTest() {
        Mockito
                .when(itemStorage.save(any()))
                .thenReturn(item1);
        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);
        Mockito
                .when(itemStorage.findById(any()))
                .thenReturn(Optional.ofNullable(item1));

        Item itemGet = itemService.updateItem(1, 1, item2);
        Mockito.verify(itemStorage).save(any());

        assertEquals(item2.getName(), itemGet.getName());

        verify(itemStorage, times(1)).save(any());
    }

    @Test
    void updateItemFailOwnerTest() {
        assertThrows(NotFoundException.class, () -> itemService.updateItem(3, 1, item2));
    }

    @Test
    void updateItemWithNullFieldsTest() {
        Mockito
                .when(itemStorage.save(any()))
                .thenReturn(item1);
        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);
        Mockito
                .when(itemStorage.findById(any()))
                .thenReturn(Optional.ofNullable(item1));

        item2.setName(null);
        item2.setDescription(null);
        item2.setAvailable(null);

        Item itemGet = itemService.updateItem(1, 1, item2);
        Mockito.verify(itemStorage).save(any());

        assertEquals(item2.getId(), itemGet.getId());

        verify(itemStorage, times(1)).save(any());
    }

    @Test
    void searchItemTest() {
        Mockito
                .when(itemStorage.findAll())
                .thenReturn(List.of(item1));

        List<Item> itemsGet = itemService.search("Item");
        Mockito.verify(itemStorage).findAll();

        assertEquals(itemsGet.size(), 1);

        verify(itemStorage, atMostOnce()).findAll();
    }

    @Test
    void addCommentTest() {
        Mockito
                .when(commentStorage.save(any()))
                .thenReturn(comment);
        Mockito
                .when(itemStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(item1));
        Mockito
                .when(bookingStorage.findByItemIdAndBookerIdAndEndBefore(anyInt(), anyInt(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(userService.getUser(anyInt()))
                .thenReturn(user);

        Comment commentSaved = itemService.addComment(1, 1, commentDto);
        Mockito.verify(commentStorage).save(any());

        assertEquals(commentSaved.getText(), commentDto.getText());

        verify(commentStorage, atMostOnce()).save(any());
    }
}