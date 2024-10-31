package by.dudkin.passenger.rest.controller;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import by.dudkin.passenger.service.PassengerService;
import by.dudkin.passenger.util.ApplicationTestConfig;
import by.dudkin.passenger.util.ErrorMessages;
import by.dudkin.passenger.util.TestDataGenerator;
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

import java.util.ArrayList;
import java.util.List;

import static by.dudkin.common.enums.PaymentMethod.CASH;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private static final String URI_PASSENGERS = "/passengers";
    private static final String URI_ID_ONE = "/passengers/1";
    private static final String URI_UNDEFINED_ID = "/passengers/999";

    @BeforeEach
    void initPassengers() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(passengerRestController)
                .build();
        passengers = new ArrayList<>();


        Passenger passenger = TestDataGenerator.createRandomPassenger();
        passenger.setId(1L);
        passengers.add(passenger);

        passenger = TestDataGenerator.createRandomPassenger();
        passenger.setId(2L);
        passengers.add(passenger);

        passenger = TestDataGenerator.createRandomPassenger();
        passenger.setId(3L);
        passengers.add(passenger);
    }

    @Test
    void testGetPassengerSuccess() throws Exception {
        given(this.passengerService.findById(1)).willReturn(passengerMapper.toPassengerDto(passengers.getFirst()));

        this.mockMvc.perform(get(URI_ID_ONE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(passengers.getFirst().getUsername()));
    }

    @Test
    void testGetPassengerNotFound() throws Exception {
        given(this.passengerService.findById(999)).willThrow(PassengerNotFoundException.class);

        this.mockMvc.perform(get(URI_UNDEFINED_ID)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllPassengersSuccess() throws Exception {
        given(this.passengerService.findAll()).willReturn(passengerMapper.toPassengerDtos(passengers));

        this.mockMvc.perform(get(URI_PASSENGERS)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].username").value(passengers.getFirst().getUsername()))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].username").value(passengers.get(1).getUsername()));
    }

    @Test
    void testGetAllPassengersWithNoPassengersShouldReturnOk() throws Exception {
        passengers.clear();
        given(this.passengerService.findAll()).willReturn(passengerMapper.toPassengerDtos(passengers));

        this.mockMvc.perform(get(URI_PASSENGERS)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void testCreatePassengerSuccess() throws Exception {
        PassengerFieldsDto newPassenger = new PassengerFieldsDto(
                TestDataGenerator.randomUsername(),
                TestDataGenerator.randomPassword(),
                TestDataGenerator.randomEmail(),
                passengers.getFirst().getInfo(),
                CASH
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassenger(newPassenger));

        this.mockMvc.perform(post(URI_PASSENGERS)
                        .content(newPassengerAsJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
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

        this.mockMvc.perform(post(URI_PASSENGERS)
                        .content(newPassengerAsJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePassengerSuccess() throws Exception {
        PassengerDto firstDto = passengerMapper.toPassengerDto(passengers.getFirst());
        PassengerFieldsDto updated = new PassengerFieldsDto(
                TestDataGenerator.randomUsername(),
                firstDto.password(),
                firstDto.email(),
                firstDto.info(),
                firstDto.preferredPaymentMethod()
        );
        given(this.passengerService.update(1, updated)).willReturn(firstDto);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(updated);

        this.mockMvc.perform(put(URI_ID_ONE)
                        .content(newPassengerAsJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePassengerError() throws Exception {
        Passenger newPassenger = passengers.getFirst();
        newPassenger.setUsername(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassengerDto(newPassenger));

        this.mockMvc.perform(put(URI_ID_ONE)
                        .content(newPassengerAsJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeletePassengerSuccess() throws Exception {
        Passenger newPassenger = passengers.getFirst();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newPassengerAsJSON = mapper.writeValueAsString(passengerMapper.toPassengerDto(newPassenger));
        given(this.passengerService.findById(1)).willReturn(passengerMapper.toPassengerDto(passengers.getFirst()));

        this.mockMvc.perform(delete(URI_ID_ONE)
                        .contentType(newPassengerAsJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePassengerError() throws Exception {
        doThrow(new PassengerNotFoundException(ErrorMessages.PASSENGER_NOT_FOUND))
                .when(passengerService).delete(999L);

        this.mockMvc.perform(delete(URI_UNDEFINED_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

}
