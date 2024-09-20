package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookingJpaRepository extends JpaRepository<Booking, Integer> {

    /**
     * Поиск бронирований пользователя.
     *
     * @param booker_id Идентификатор пользователя.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdOrderByStartDesc(int booker_id);

    /**
     * Поиск текущих бронирований пользователя.
     *
     * @param booker_id Идентификатор пользователя.
     * @param start     Текущее время.
     * @param end       Текущее время.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int booker_id,
                                                                                 LocalDateTime start,
                                                                                 LocalDateTime end);

    /**
     * Поиск бронирований пользователя, окончившихся ранее заданного времени.
     *
     * @param booker_id Идентификатор пользователя.
     * @param time      Время, до которого нужно найти закончившиеся бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(int booker_id,
                                                                    LocalDateTime time);

    /**
     * Поиск бронирований пользователя, начавшиеся позже заданного времени.
     *
     * @param booker_id Идентификатор пользователя.
     * @param time      Время, после которого нужно найти начавшиеся бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(int booker_id,
                                                                     LocalDateTime time);

    /**
     * Поиск бронирований пользователя с заданным статусом.
     *
     * @param booker_id Идентификатор пользователя.
     * @param status    Статус бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdAndStatusLikeOrderByStartDesc(int booker_id, BookingStatus status);

    /**
     * Поиск бронирований вещей владельца.
     *
     * @param owner_id Идентификатор владельца.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdAll(int owner_id);

    /**
     * Поиск текущих бронирований вещей владельца.
     *
     * @param owner_id Идентификатор владельца.
     * @param time     Текущее время.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1 AND " +
            " b.start < ?2 AND  b.end > ?2" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdCurrent(int owner_id, LocalDateTime time);

    /**
     * Поиск бронирований вещей владельца, окончившихся ранее заданного времени.
     *
     * @param owner_id Идентификатор владельца.
     * @param time     Время, до которого нужно найти закончившиеся бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1 AND " +
            "b.end < ?2" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdPast(int owner_id, LocalDateTime time);

    /**
     * Поиск бронирований вещей владельца, начавшиеся позже заданного времени.
     *
     * @param owner_id Идентификатор владельца.
     * @param time     Время, после которого нужно найти начавшиеся бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1 AND " +
            "b.start > ?2" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdFuture(int owner_id, LocalDateTime time);

    /**
     * Поиск бронирований вещей владельца с заданным статусом.
     *
     * @param owner_id Идентификатор владельца.
     * @param status   Статус бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1 AND " +
            "b.status = ?2" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdStatus(int owner_id, BookingStatus status);

    /**
     * Поиск бронирований для списка вещей.
     *
     * @param item_id Список идентификаторов вещей.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByItemIdIn(Collection<Integer> item_id);

    /**
     * Поиск бронирований для вещи.
     *
     * @param item_id Идентификатор вещи.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByItemId(int item_id);

    /**
     * Поиск бронирований пользователя для вещи.
     *
     * @param item_id   Идентификатор вещи.
     * @param booker_id Идентификатор пользователя.
     * @param end       Время окончания аренды.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByItemIdAndBookerIdAndEndBefore(int item_id, int booker_id, LocalDateTime end);

}
