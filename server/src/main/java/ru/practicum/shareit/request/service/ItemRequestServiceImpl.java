package ru.practicum.shareit.request.service;

import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestJpaRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemRequestJpaRepository itemRequestJpaRepository;

    UserService userService;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestJpaRepository itemRequestJpaRepository, UserService userService) {
        this.itemRequestJpaRepository = itemRequestJpaRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ItemRequest addRequest(int requesterId, ItemRequest request) {
        request.setRequester(userService.getUser(requesterId));
        request.setCreated(LocalDateTime.now());
        return itemRequestJpaRepository.save(request);
    }

    @Override
    public ItemRequest getRequest(int requestId) {
        return itemRequestJpaRepository.findById(requestId)
                .orElseThrow(() -> new ValidationException("Error: item request " + requestId + " is not found."));
    }

    @Override
    public List<ItemRequest> getAllRequest() {
        return itemRequestJpaRepository.findAll();
    }

    @Override
    public List<ItemRequest> getAllRequestByRequester(int requesterId) {
        userService.getUser(requesterId);
        return itemRequestJpaRepository.findByRequesterId(requesterId);
    }
}
