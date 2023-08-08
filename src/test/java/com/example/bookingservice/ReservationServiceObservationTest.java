package com.example.bookingservice;

import com.example.bookingservice.configuration.EnableTestObservation;
import com.example.bookingservice.service.ReservationService;
import io.micrometer.observation.tck.TestObservationRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.micrometer.observation.tck.TestObservationRegistryAssert.assertThat;

@SpringBootTest
@EnableTestObservation
public class ReservationServiceObservationTest {

    @Autowired
    ReservationService service;
    @Autowired
    TestObservationRegistry registry;

    @Test
    void testObservation() {
        // invoke service
        service.getAll();
        assertThat(registry)
                .hasObservationWithNameEqualTo("getAll")
                .that()
                .hasBeenStarted()
                .hasBeenStopped();
    }
}
