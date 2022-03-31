package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotDAOTest {
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpotDAO parkingSpotDAO;

    private final ParkingType parkingType = ParkingType.CAR;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void gettingNextSlotAvailableTest() {
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(parkingType));
    }

    @Test
    public void updatingParkingSlotTest() {
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(parkingType), parkingType, false);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot(parkingType));
    }

}