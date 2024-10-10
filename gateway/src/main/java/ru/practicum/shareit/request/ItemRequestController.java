package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.add(itemRequestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable @Positive int requestId) {
        return itemRequestClient.get(requestId);
    }

    @GetMapping()
    public ResponseEntity<Object> getByRequester(@RequestHeader("X-Sharer-User-Id") @Positive int userId) {
        return itemRequestClient.getByRequester(userId);
    }

}