package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotDAOTest {
    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeEach
    private void setUpPerTest() {
        parkingSpotDAO = new ParkingSpotDAO();
        dataBasePrepareService = new DataBasePrepareService();
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void closeTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    @DisplayName("Test the method getNextAvailableSlot() with car")
    public void getNextAvailableSlotCarTest() {
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    @DisplayName("Test the method getNextAvailableSlot() with bike")
    public void getNextAvailableSlotBikeTest() {
        assertEquals(4, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
    }
    @Test
    @DisplayName("Test the method getNextAvailableSlot() with null ParkingType")
    public void getNextAvailableSlotNullParkingTypeTest() {
        assertThrows(NullPointerException.class, () ->parkingSpotDAO.getNextAvailableSlot(null));
    }

    @Test
    @DisplayName("Test the method updateParking() with car")
    public void updateParkingTest() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));

    }
    @Test
    @DisplayName("Test the method getNextAvailableSlot() with null ParkingType")
    public void updateParkingWithNoParkingTypeTest(){
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    @DisplayName("Test the method getNextAvailableSlot() with negative Parking Slot")
    public void updateParkingWithNegativeParkingSlotTest(){
        ParkingSpot parkingSpot = new ParkingSpot(-1, ParkingType.CAR, false);
        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }

    /* Test ParkingSpot class*/
    @Test
    @DisplayName("Test the method equals()")
    public void ParkingSpotEqualsTrueTest() {
        ParkingSpot parkingSpot = new ParkingSpot(0, ParkingType.BIKE, true);
        assertEquals(parkingSpot, parkingSpot);
    }

    @Test
    @DisplayName("Test the method equals()")
    public void ParkingSpotEqualsFalseTest() {
        ParkingSpot parkingSpotBike = new ParkingSpot(0, ParkingType.BIKE, true);
        ParkingSpot parkingSpotCar = new ParkingSpot(1, ParkingType.CAR, true);
        assertNotEquals(parkingSpotCar, parkingSpotBike);
    }
}