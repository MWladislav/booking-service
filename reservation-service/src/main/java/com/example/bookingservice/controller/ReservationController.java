package com.example.bookingservice.controller;

import com.example.bookingservice.dto.ReservationRecord;
import com.example.bookingservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public Collection<ReservationRecord> getAll() {
        return reservationService.getAll();
    }

    @GetMapping(params = {"startDate", "endDate"})
    public Collection<ReservationRecord> getAllBetweenDates(
            @RequestParam OffsetDateTime startDate,
            @RequestParam OffsetDateTime endDate
    ) {
        return reservationService.getAllBetweenDates(startDate, endDate);
    }

    @GetMapping("/{id}")
    public ReservationRecord byId(@PathVariable Long id) {
        return reservationService.getById(id);
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody ReservationRecord reservation) {
        final Long id = reservationService.createReservation(reservation);
        return ResponseEntity
                .created(URI.create("localhost:8080/reservation/%d".formatted(id)))
                .build();
    }

    @PutMapping("/{id}/reschedule")
    public ReservationRecord reschedule(@PathVariable Long id, @RequestParam OffsetDateTime newDate) {
        return reservationService.rescheduleReservation(id, newDate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
