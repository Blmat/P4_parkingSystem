package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {
    }


    //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        int car = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        ticket.setDiscount(true);

        assertNotNull(ticket);
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        assertNotNull(parkingSpot);


        assertFalse(parkingSpot.isAvailable());
        assertEquals(car, parkingSpot.getId());
        assertTrue(ticketDAO.getVehicleRegNumberFromPastUsers("ABCDEF"));
    }


    //TODO: check that the fare generated and out time are populated correctly in the database
    @Test
    public void testParkingLotExit() throws InterruptedException {
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processExitingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        Date outTime = new Date();
        ticketDAO.getVehicleRegNumberFromPastUsers("ABCDEF");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setDiscount(true);

        double expected = Math.round(24 * Fare.CAR_RATE_PER_HOUR * Fare.DISCOUNT_FOR_REGULAR_CUSTOMER * 100) / 100d;
        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        fareCalculatorService.calculateFare(ticket);
        assertNotNull(ticket);
        assertNotNull(ticket.getOutTime());
        assertEquals(expected, ticket.getPrice());
    }

    @Test
    public void testParkingABike() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("BIKE");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        int bike = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        parkingService.processIncomingVehicle();

        Ticket ticket = ticketDAO.getTicket("BIKE");

        assertNotNull(ticket);
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        assertNotNull(parkingSpot);
        assertFalse(parkingSpot.isAvailable());
        assertEquals(bike, parkingSpot.getId());
    }

    @Test
    public void testParkingLotExitBike() throws Exception {
        testParkingABike();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();

        Ticket ticket = ticketDAO.getTicket("BIKE");
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ticketDAO.getVehicleRegNumberFromPastUsers("BIKE");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setDiscount(false);

        FareCalculatorService fareCalculatorService = new FareCalculatorService();
        fareCalculatorService.calculateFare(ticket);
        assertNotNull(ticket);
        assertNotNull(ticket.getOutTime());
        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }
}
