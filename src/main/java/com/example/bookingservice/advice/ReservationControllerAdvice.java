package com.example.bookingservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReservationControllerAdvice {
    @ExceptionHandler
    ProblemDetail handleIllegalStateException(IllegalStateException exception) {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setDetail(exception.getLocalizedMessage());
        return pd;
    }
}
