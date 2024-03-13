package com.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.evaluation.controller.ReservationController;
import com.evaluation.entity.Reservation;
import com.evaluation.repository.ReservationRepository;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    void testGetAllReservations() throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation("P306", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "karim"));
        when(reservationRepository.findAll()).thenReturn(reservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));
    }

    @Test
    void testCreateReservation() throws Exception {
        Reservation reservation = new Reservation("P206", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "hamza");

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"salle\": \"Salle A\", \"debut\": \"2024-03-14T10:00:00\", \"fin\": \"2024-03-14T11:00:00\", \"utilisateur\": \"Utilisateur A\" }"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salle").value("Salle A"));
    }


}
