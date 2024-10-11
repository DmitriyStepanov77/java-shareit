package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    UNKNOWN;

    public static BookingState convert(String str) {
        for (BookingState bookingState : BookingState.values()) {
            if (bookingState.toString().equals(str)) {
                return bookingState;
            }
        }
        return UNKNOWN;
    }

}
