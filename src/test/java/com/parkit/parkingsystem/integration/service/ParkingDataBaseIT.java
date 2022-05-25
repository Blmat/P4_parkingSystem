package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.DataBaseTestConfig;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private final String vehicleRegNumber = "ABCDEF";


    @Mock
    private static InputReaderUtil inputReaderUtil;
    private Ticket ticket;
    private Integer parkingSlot;
    private ParkingService parkingService;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        ticket = null;
        parkingSlot = null;
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        dataBasePrepareService.clearDataBaseEntries();
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
    }

    @AfterAll
    private static void tearDown() {
    }

    private void vehicleIncoming(ParkingType parkingType) {
        int selection = (parkingType == ParkingType.CAR)
                ? 1
                : 2;
        when(inputReaderUtil.readSelection()).thenReturn(selection);

        parkingService.processIncomingVehicle();
        parkingSlot = parkingSpotDAO.getNextAvailableSlot(parkingType);
        ticket = ticketDAO.getTicket(vehicleRegNumber);
    }


    @Test
    public void testParkingACar() {
        //TODO: check that a ticket is actually saved in DB and Parking table is updated with availability
        int initialParkingSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        vehicleIncoming(ParkingType.CAR);

        assertNotNull(parkingSlot);
        assertTrue(parkingSlot > 0);
        assertNotEquals(initialParkingSlot, parkingSlot);
        assertTrue(initialParkingSlot < parkingSlot);

        assertNotNull(ticket);
        assertNotNull(ticket.getInTime());
        assertNull(ticket.getOutTime());
    }

    @Test
    public void testParkingABike() {

        int initialParkingSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        vehicleIncoming(ParkingType.BIKE);

        assertNotNull(parkingSlot);
        assertTrue(parkingSlot > 0);
        assertNotEquals(initialParkingSlot, parkingSlot);
        assertTrue(initialParkingSlot < parkingSlot);

        assertNotNull(ticket);
        assertNotNull(ticket.getInTime());
        assertNull(ticket.getOutTime());
    }

    @Test
    public void testParkingLotExit() {

        testParkingACar();
        long initTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        while (currentTime - initTime != 1000) {
            currentTime = System.currentTimeMillis();
        }
        parkingService.processExitingVehicle();
        // TODO: check that the fare generated and out time are populated correctly in the database
        ticket = ticketDAO.getTicket(vehicleRegNumber);
        assertNotNull(ticket);
        assertNotEquals(null, ticket.getOutTime());
        assertEquals(0, ticket.getPrice());
    }
}
