package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    @Autowired
    ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> addItemRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                                         @RequestBody ItemRequest itemRequest) {
        return ResponseEntity.ok()
                .body(ItemRequestDtoMapper.mapToItemRequestDto(itemRequestService.addRequest(userId, itemRequest)));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getItemRequestsByRequester(@RequestHeader("X-Sharer-User-Id") int userId) {
        return ResponseEntity.ok()
                .body(itemRequestService.getAllRequestByRequester(userId).stream()
                        .map(ItemRequestDtoMapper::mapToItemRequestDto).toList());
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getItemRequest(@PathVariable int requestId) {
        return ResponseEntity.ok()
                .body(ItemRequestDtoMapper.mapToItemRequestDto(itemRequestService.getRequest(requestId)));
    }
}
