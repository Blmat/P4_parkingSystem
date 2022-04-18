package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private final DecimalFormat df = new DecimalFormat("#.##");
    private final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private Connection con = dataBaseTestConfig.getConnection();
    private ParkingService parkingService;
    private Logger logger;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private ParkingSpot parkingSpot;
    @Mock
    private Ticket ticket;


    public ParkingServiceTest() throws SQLException, ClassNotFoundException {
    }

    @BeforeEach
    private void setUpPerTest() throws SQLException, ClassNotFoundException {
        con = dataBaseTestConfig.getConnection();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    }

    @AfterEach
    private void clearConnection() throws SQLException {
        con.close();
    }

    @Test
    public void processValidIncomingVehicleTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("MyCar");
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        parkingService.processIncomingVehicle();

        verify(inputReaderUtil, times(1)).readSelection();
        verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    @DisplayName("Test to catch the exception in processIncomingVehicle() ")
    public void processInvalidIncomingVehicleTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        when(ticketDAO.saveTicket(ticket)).thenReturn(false);

        parkingService.processIncomingVehicle();

        assertThrows(Exception.class, () -> logger.getName());
    }

    @Test
    @DisplayName("Test the getVehicleType() with an unknown vehicle type")
    public void getVehicleTypeUnknownTest() {
        when(inputReaderUtil.readSelection()).thenReturn(4);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        assertThrows(IllegalArgumentException.class, () -> parkingService.getVehichleType());
    }

    @Test
    @DisplayName("Test if the updateParking() method, the getTicket() method and the updateTicket() method are invoked")
    public void processExitingVehicleTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket(false);
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processExitingVehicle();

        verify(ticketDAO, times(1)).getTicket(anyString());
        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    @DisplayName("Test method getVehicle() with Car ")
    public void getNextCarParkingNumberIfAvailableTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(2);

        parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, times(1)).readSelection();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
        assertEquals(2, parkingSpot.getId());
    }

    @Test
    @DisplayName("Test method getVehicle() with Bike ")
    public void getNextBikeParkingNumberIfAvailableTest() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);

        parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, times(1)).readSelection();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(new ParkingSpot(4, ParkingType.BIKE, false), parkingSpot);
    }

    @Test
    @DisplayName("Test the method getNextParkingNumberIfAvailable() and catch exception")
    public void getNextParkingNumberIfNotAvailableTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

        parkingService.getNextParkingNumberIfAvailable();

        assertThrows(Exception.class, () -> logger.getName());
    }
}

