package com.example.bookingservice.controller;

import com.example.bookingservice.dto.ReservationRecord;
import com.example.bookingservice.service.ReservationService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.function.Supplier;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final ObservationRegistry registry;

    public ReservationController(final ReservationService reservationService, final ObservationRegistry registry) {
        this.reservationService = reservationService;
        this.registry = registry;
    }

    private <T> T wrapWithObservation(String observationName, Supplier<T> supplier) {
        return Observation
                .createNotStarted(observationName, this.registry)
                .observe(supplier);
    }

    private void wrapWithObservation(String observationName, Runnable runnable) {
        Observation
                .createNotStarted(observationName, this.registry)
                .observe(runnable);
    }

    @GetMapping
    public Collection<ReservationRecord> getAll() {
        return wrapWithObservation("all", reservationService::getAll);
    }

    @GetMapping(params = {"startDate", "endDate"})
    public Collection<ReservationRecord> getAllBetweenDates(
            @RequestParam OffsetDateTime startDate,
            @RequestParam OffsetDateTime endDate
    ) {
        return wrapWithObservation(
                "allBetweenDates",
                () -> reservationService.getAllBetweenDates(startDate, endDate)
        );
    }

    @GetMapping("/{id}")
    public ReservationRecord byId(@PathVariable Long id) {
        return wrapWithObservation("byId", () -> reservationService.getById(id));
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody ReservationRecord reservation) {
        final Long id = wrapWithObservation(
                "create",
                () -> reservationService.createReservation(reservation)
        );
        return ResponseEntity
                .created(URI.create("localhost:8080/reservation/%d".formatted(id)))
                .build();
    }

    @PutMapping("/{id}/reschedule")
    public ReservationRecord reschedule(@PathVariable Long id, @RequestParam OffsetDateTime newDate) {
        return wrapWithObservation(
                "reschedule",
                () -> reservationService.rescheduleReservation(id, newDate)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        wrapWithObservation("cancel", () -> reservationService.cancelReservation(id));
        return ResponseEntity.noContent().build();
    }
}
