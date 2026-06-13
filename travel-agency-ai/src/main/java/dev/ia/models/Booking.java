package dev.ia.models;

import dev.ia.enums.BookingStatus;
import dev.ia.enums.Category;

import java.time.LocalDate;

public record Booking(
        Long id,
        String customerName,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatus status,
        Category category
) {
}
