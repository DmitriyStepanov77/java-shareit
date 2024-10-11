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
     * @param bookerId Идентификатор пользователя.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdOrderByStartDesc(int bookerId);

    /**
     * Поиск текущих бронирований пользователя.
     *
     * @param bookerId Идентификатор пользователя.
     * @param start     Текущее время.
     * @param end       Текущее время.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int bookerId,
                                                                                 LocalDateTime start,
                                                                                 LocalDateTime end);

    /**
     * Поиск бронирований пользователя, окончившихся ранее заданного времени.
     *
     * @param bookerId Идентификатор пользователя.
     * @param time      Время, до которого нужно найти закончившиеся бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(int bookerId,
                                                                    LocalDateTime time);

    /**
     * Поиск бронирований пользователя, начавшиеся позже заданного времени.
     *
     * @param bookerId Идентификатор пользователя.
     * @param time      Время, после которого нужно найти начавшиеся бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(int bookerId,
                                                                     LocalDateTime time);

    /**
     * Поиск бронирований пользователя с заданным статусом.
     *
     * @param bookerId Идентификатор пользователя.
     * @param status    Статус бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByBookerIdAndStatusLikeOrderByStartDesc(int bookerId, BookingStatus status);

    /**
     * Поиск бронирований вещей владельца.
     *
     * @param ownerId Идентификатор владельца.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdAll(int ownerId);

    /**
     * Поиск текущих бронирований вещей владельца.
     *
     * @param ownerId Идентификатор владельца.
     * @param time     Текущее время.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1 AND " +
            " b.start < ?2 AND  b.end > ?2" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdCurrent(int ownerId, LocalDateTime time);

    /**
     * Поиск бронирований вещей владельца, окончившихся ранее заданного времени.
     *
     * @param ownerId Идентификатор владельца.
     * @param time     Время, до которого нужно найти закончившиеся бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1 AND " +
            "b.end < ?2" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdPast(int ownerId, LocalDateTime time);

    /**
     * Поиск бронирований вещей владельца, начавшиеся позже заданного времени.
     *
     * @param ownerId Идентификатор владельца.
     * @param time     Время, после которого нужно найти начавшиеся бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1 AND " +
            "b.start > ?2" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdFuture(int ownerId, LocalDateTime time);

    /**
     * Поиск бронирований вещей владельца с заданным статусом.
     *
     * @param ownerId Идентификатор владельца.
     * @param status   Статус бронирования.
     * @return список объектов, содержащий данные о бронированиях.
     */
    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1 AND " +
            "b.status = ?2" +
            " order by b.start desc")
    public List<Booking> findByOwnerIdStatus(int ownerId, BookingStatus status);

    /**
     * Поиск бронирований для списка вещей.
     *
     * @param itemId Список идентификаторов вещей.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByItemIdIn(Collection<Integer> itemId);

    /**
     * Поиск бронирований для вещи.
     *
     * @param itemId Идентификатор вещи.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByItemId(int itemId);

    /**
     * Поиск бронирований пользователя для вещи.
     *
     * @param itemId   Идентификатор вещи.
     * @param bookerId Идентификатор пользователя.
     * @param end       Время окончания аренды.
     * @return список объектов, содержащий данные о бронированиях.
     */
    public List<Booking> findByItemIdAndBookerIdAndEndBefore(int itemId, int bookerId, LocalDateTime end);

}
