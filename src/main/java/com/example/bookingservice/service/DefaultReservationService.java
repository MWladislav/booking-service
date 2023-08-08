package com.example.bookingservice.service;

import com.example.bookingservice.domain.Reservation;
import com.example.bookingservice.dto.ReservationRecord;
import com.example.bookingservice.observation.WithObservation;
import com.example.bookingservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultReservationService implements ReservationService {

    private final ReservationRepository reservationRepository;

    public DefaultReservationService(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @WithObservation
    @Override
    public List<ReservationRecord> getAll() {
        return mapToReservationRecord(reservationRepository.findAll());
    }

    @WithObservation
    @Override
    public List<ReservationRecord> getAllBetweenDates(
            final OffsetDateTime startDate,
            final OffsetDateTime endDate
    ) {
        return mapToReservationRecord(reservationRepository.findAllByDateBetween(startDate, endDate));
    }

    @WithObservation
    @Override
    public ReservationRecord getById(final Long id) {
        Assert.state(id > 0, "Reservation id should be positive!");
        var reservation = reservationRepository.findById(id).orElseThrow();
        return mapToReservationRecord(reservation);
    }

    @WithObservation
    @Override
    public Long createReservation(final ReservationRecord reservation) {
        var entity = new Reservation();
        entity.setDetails(reservation.details());
        entity.setTableNumber(reservation.tableNumber());
        entity.setDate(reservation.date());
        return reservationRepository.save(entity).getId();
    }

    @WithObservation
    @Override
    public ReservationRecord rescheduleReservation(Long id, final OffsetDateTime newDate) {
        Assert.state(id > 0, "Reservation id should be positive!");
        final Reservation reservation = reservationRepository.findById(id).orElseThrow();

        reservation.setDate(newDate);
        return mapToReservationRecord(reservationRepository.save(reservation));
    }

    @WithObservation
    @Override
    public void cancelReservation(Long id) {
        Assert.state(id > 0, "Reservation id should be positive!");
        reservationRepository.deleteById(id);
    }

    private static List<ReservationRecord> mapToReservationRecord(final List<Reservation> reservations) {
        return reservations
                .stream()
                .map(DefaultReservationService::mapToReservationRecord)
                .collect(Collectors.toList());
    }

    private static ReservationRecord mapToReservationRecord(final Reservation reservation) {
        return new ReservationRecord(
                reservation.getDetails(),
                reservation.getDate(),
                reservation.getTableNumber()
        );
    }
}
