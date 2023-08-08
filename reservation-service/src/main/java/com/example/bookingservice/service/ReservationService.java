package com.example.bookingservice.service;

import com.example.bookingservice.dto.ReservationRecord;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservationService {
    List<ReservationRecord> getAll();
    List<ReservationRecord> getAllBetweenDates(OffsetDateTime startDate, final OffsetDateTime endDate);
    ReservationRecord getById(Long id);
    Long createReservation(ReservationRecord reservation);
    ReservationRecord rescheduleReservation(Long id, OffsetDateTime newDate);
    void cancelReservation(Long id);

}
