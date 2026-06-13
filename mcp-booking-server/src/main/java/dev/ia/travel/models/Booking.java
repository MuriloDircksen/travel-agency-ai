package dev.ia.travel.models;

import dev.ia.travel.enums.BookingStatus;
import dev.ia.travel.enums.Category;

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
