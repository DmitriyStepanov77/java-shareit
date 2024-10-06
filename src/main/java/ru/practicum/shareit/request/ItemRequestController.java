package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    ItemRequestServiceImpl itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestServiceImpl itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @Valid @RequestBody ItemRequest itemRequest) {
        return ItemRequestDtoMapper.mapToItemRequestDto(itemRequestService.addRequest(userId, itemRequest));
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestService.getAllRequestByRequestor(userId).stream()
                .map(ItemRequestDtoMapper::mapToItemRequestDto).toList();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@PathVariable int requestId) {
        return ItemRequestDtoMapper.mapToItemRequestDto(itemRequestService.getRequest(requestId));
    }
}
