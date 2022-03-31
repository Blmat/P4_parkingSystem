package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingServiceIncomingBikeTest {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private String vehicleRegNumber = "Bike01";
    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeAll
    private static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
    }

    @BeforeEach
    private void setUpPerTest() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(2);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
            when(inputReaderUtil.readSelection()).thenReturn(2);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    void processIncomingVehicle() {
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }
}