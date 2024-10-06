package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestJpaRepository extends JpaRepository<ItemRequest, Integer> {

    public List<ItemRequest> findByRequesterId(int requesterId);
}
