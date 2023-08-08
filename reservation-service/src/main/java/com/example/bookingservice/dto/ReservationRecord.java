package com.example.bookingservice.dto;

import java.time.OffsetDateTime;

public record ReservationRecord(String details, OffsetDateTime date, Integer tableNumber) {
}
