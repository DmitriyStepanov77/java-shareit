package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Integer> {

    /**
     * Поиск комментариев к вещи.
     *
     * @param itemId Идентификатор вещи.
     * @return список объектов, содержащий данные о комментариях.
     */
    public List<Comment> findByItemId(int itemId);

}
