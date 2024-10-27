package by.dudkin.passenger.rest.controller;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.service.ApplicationTestConfig;
import by.dudkin.passenger.service.PassengerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static by.dudkin.common.enums.PaymentMethod.CASH;
import static java.math.BigDecimal.valueOf;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
class PassengerRestControllerTests {

    @Autowired
    private PassengerRestController passengerRestController;

    @Autowired
    private PassengerMapper passengerMapper;

    @MockBean
    private PassengerService passengerService;

    private MockMvc mockMvc;

    private List<Passenger> passengers;

    @BeforeEach
    void initPassengers() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(passengerRestController)
            .build();
        passengers = new ArrayList<>();

        Passenger passenger = Passenger.builder()
            .username("evgeny")
            .email("jeka@gmail.com")
            .password("password")
            .info(PersonalInfo.builder()
                .firstName("Evgeny")
                .lastName("Borisov")
                .phone("Terminator")
                .dateOfBirth(LocalDate.of(1978, 1, 1))
                .build())
            .preferredPaymentMethod(CASH)
            .balance(valueOf(2000.00))
            .build();
        passenger.setId(1L);
        passengers.add(passenger);

        passenger = Passenger.builder()
            .username("ivan")
            .email("inponomarev@gmail.com")
            .password("password")
            .info(PersonalInfo.builder()
                .firstName("Ivan")
                .lastName("Ponomarev")
                .phone("MPTI")
                .dateOfBirth(LocalDate.of(1984, 1, 1))
                .build())
            .preferredPaymentMethod(CASH)
            .balance(valueOf(2000.00))
            .build();
        passenger.setId(2L);
        passengers.add(passenger);

        passenger = Passenger.builder()
            .username("gnome")
            .email("gnome@gmail.com")
            .password("password")
            .info(PersonalInfo.builder()
                .firstName("Gnome")
                .lastName("Union")
                .phone("Balaa")
                .dateOfBirth(LocalDate.of(1950, 1, 1))
                .build())
            .preferredPaymentMethod(CASH)
            .balance(valueOf(2000.00))
            .build();
        passenger.setId(3L);
        passengers.add(passenger);
    }

    @Test
    void testGetPassengerSuccess() throws Exception {
        given(this.passengerService.findById(1)).willReturn(passengers.getFirst());
        this.mockMvc.perform(get("/passengers/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("evgeny"));
    }

    @Test
    void testGetPassengerNotFound() throws Exception {
        given(this.passengerService.findById(-1)).willReturn(null);
        this.mockMvc.perform(get("/passengers/999")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllPassengersSuccess() throws Exception {
        given(this.passengerService.findAll()).willReturn(passengers);
        this.mockMvc.perform(get("/passengers/")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[0].username").value("evgeny"))
            .andExpect(jsonPath("$.[1].id").value(2))
            .andExpect(jsonPath("$.[1].username").value("ivan"));
    }

    @Test
    void testGetAllPassengersNotFound() throws Exception {
        passengers.clear();
        given(this.passengerService.findAll()).willReturn(passengers);
        this.mockMvc.perform(get("/passengers/")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePassengerSuccess() throws Exception {
        Passenger newPassenger = passengers.getFirst();
        newPassenger.setId(999L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassengerDto(newPassenger));
        this.mockMvc.perform(post("/passengers")
                .content(newPassengerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    void testCreatePassengerError() throws Exception {
        Passenger newPassenger = passengers.getFirst();
        newPassenger.setId(null);
        newPassenger.setUsername(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassengerDto(newPassenger));
        this.mockMvc.perform(post("/passengers")
                .content(newPassengerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePassengerSuccess() throws Exception {
        given(this.passengerService.findById(1)).willReturn(passengers.getFirst());
        Passenger newPassenger = passengers.getFirst();
        newPassenger.setUsername("Terminator");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassengerDto(newPassenger));
        this.mockMvc.perform(put("/passengers/1")
                .content(newPassengerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/passengers/1")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("Terminator"));
    }

    @Test
    void testUpdatePassengerError() throws Exception {
        Passenger newPassenger = passengers.getFirst();
        newPassenger.setUsername(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassengerDto(newPassenger));
        this.mockMvc.perform(put("/passengers/1")
                .content(newPassengerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testDeletePassengerSuccess() throws Exception {
        Passenger newPassenger = passengers.getFirst();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassengerDto(newPassenger));
        given(this.passengerService.findById(1)).willReturn(passengers.getFirst());
        this.mockMvc.perform(delete("/passengers/1")
                .contentType(newPassengerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePassengerError() throws Exception {
        Passenger newPassenger = passengers.getFirst();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassengerDto(newPassenger));
        given(this.passengerService.findById(-1)).willReturn(null);
        this.mockMvc.perform(delete("/passengers/999")
                .contentType(newPassengerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }

}
