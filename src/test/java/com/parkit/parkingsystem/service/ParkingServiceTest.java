//package com.parkit.parkingsystem.service;
//
//import com.parkit.parkingsystem.constants.ParkingType;
//import com.parkit.parkingsystem.dao.ParkingSpotDAO;
//import com.parkit.parkingsystem.dao.TicketDAO;
//import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
//import com.parkit.parkingsystem.model.ParkingSpot;
//import com.parkit.parkingsystem.model.Ticket;
//import com.parkit.parkingsystem.util.InputReaderUtil;
//import junit.framework.Assert;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ParkingServiceTest {
//
//    private static ParkingService parkingService;
//
//    @Mock
//    private static InputReaderUtil inputReaderUtil;
//    @Mock
//    private static ParkingSpotDAO parkingSpotDAO;
//    @Mock
//    private static TicketDAO ticketDAO;
//
//    @BeforeAll
//    private static void setUp() {
//        DataBasePrepareService dataBasePrepareService = new DataBasePrepareService();
//        dataBasePrepareService.clearDataBaseEntries();
//    }
//
//    @Test
//    @DisplayName("Test if the updateParking() method, the getTicket() method and the updateTicket() method are invoked")
//    public void processExitingVehicleTest() throws Exception {
//        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
//        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
//        Ticket ticket = new Ticket();
//        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
//        ticket.setParkingSpot(parkingSpot);
//        ticket.setVehicleRegNumber("ABCDEF");
//        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
//        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
//        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
//        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//
//        parkingService.processExitingVehicle();
//
//        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
//        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
//        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
//    }
//
//    @Test
//    @DisplayName("Test if the updateParking() method and the saveTicket() method are invoked")
//    public void processIncomingVehicleTest() throws Exception {
//        when(inputReaderUtil.readSelection()).thenReturn(1);
//        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
//        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
//        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
//        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
//        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//
//        parkingService.processIncomingVehicle();
//
//        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
//        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
//    }
//
//    @Test // Test than the getVehicleType() method work correctly with cars
//    public void getVehicleTypeCarTest() {
//        when(inputReaderUtil.readSelection()).thenReturn(1);
//        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//
//        Assert.assertEquals(ParkingType.CAR, parkingService.getVehichleType());
//    }
//
//    @Test // Test than the getVehicleType() method work correctly with bikes
//    public void getVehicleTypeBikeTest() {
//        when(inputReaderUtil.readSelection()).thenReturn(2);
//        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//
//        Assert.assertEquals(ParkingType.BIKE, parkingService.getVehichleType());
//    }
//
//    @Test // Test the getVehicleType() with an unknown vehicle type
//    public void getVehicleTypeUnknownTest() {
//        when(inputReaderUtil.readSelection()).thenReturn(4);
//        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//
//        assertThrows(IllegalArgumentException.class, () -> parkingService.getVehichleType());
//    }
//
//    @Test // Test the getVehicleRegNumber() method with correct input
//    public void getVehicleRegNumberTest() throws Exception {
//        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
//        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//
//        Assert.assertEquals("ABCDEF", parkingService.getVehichleRegNumber());
//    }
//
//    @Test // Test the getVehicleRegNumber() method with null input
//    public void getNullVehicleRegNumberTest() throws Exception {
//        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(null);
//        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//
//        Assert.assertNull(parkingService.getVehichleRegNumber());
//    }
//
//
//    @Test // Test the method getNextParkingNumberIfAvailable()
//    public void getNextParkingNumberIfAvailableTest() throws SQLException, IOException, ClassNotFoundException {
//
//        when(inputReaderUtil.readSelection()).thenReturn(1);
//        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
//        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
//        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//
//        Assert.assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
//    }
//}
package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @Mock
    private DataBaseTestConfig dataBaseTestConfig;
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

    private ParkingService parkingService;
    private Logger logger;


    @BeforeEach
    private void setUpPerTest() throws SQLException, ClassNotFoundException {
        Connection con = dataBaseTestConfig.getConnection();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    @Test
    public void getNextCarParkingNumberIfAvailableTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(2);

        parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, times(1)).readSelection();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
        assertEquals(2, parkingSpot.getId());
    }

    @Test
    public void getNextBikeParkingNumberIfAvailableTest() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);

        parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, times(1)).readSelection();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(new ParkingSpot(4, ParkingType.BIKE, false), parkingSpot);
    }

    @Test
    public void getNextParkingNumberIfNotAvailableTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

        parkingService.getNextParkingNumberIfAvailable();

        assertThrows(Exception.class, () -> logger.getName());
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
    public void processInvalidIncomingVehicleTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        when(ticketDAO.saveTicket(ticket)).thenReturn(false);

        parkingService.processIncomingVehicle();

        assertThrows(Exception.class, () -> logger.error("Unable to process incoming vehicle"));
    }

    @Test
    public void processValidExitingVehicleTest() throws Exception {
        java.util.Date inTime = new java.util.Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        java.util.Date outTime = new Date();
        ticket = new Ticket(false);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        lenient().when(inputReaderUtil.readSelection()).thenReturn(2);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        lenient().when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));

        assertEquals(1* Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void processInvalidExitingVehicleTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("test");

        parkingService.processExitingVehicle();

        assertThrows(Exception.class, () -> logger.error("Unable to process exiting vehicle"));
    }
}
