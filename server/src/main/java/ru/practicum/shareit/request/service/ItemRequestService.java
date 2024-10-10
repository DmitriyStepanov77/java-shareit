package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    public ItemRequest addRequest(int requestorId, ItemRequest request);

    public ItemRequest getRequest(int requestId);

    public List<ItemRequest> getAllRequest();

    public List<ItemRequest> getAllRequestByRequester(int requesterId);
}
