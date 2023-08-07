package com.example.bookingservice.repository;

import com.example.bookingservice.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByDateBetween(final OffsetDateTime min, final OffsetDateTime max);
}
