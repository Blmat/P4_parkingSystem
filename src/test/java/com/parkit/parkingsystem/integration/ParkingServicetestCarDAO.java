package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingServicetestCarDAO {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtilCar;

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
        when(inputReaderUtilCar.readSelection()).thenReturn(1);
        when(inputReaderUtilCar.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtilCar, parkingSpotDAO, ticketDAO);
        // DP : récuperer le no de la prochaine classe disponible pour une voiture
        int next = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        parkingService.processIncomingVehicle();
        // Etape 1 : récupérer le ticket du véhicule immatriculé "ABCDEF" ==>mock
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        // Etape 2 : vérifier l'existence du ticket
        assertNotNull(ticket);
        // Etape 3 : récupérer l'ID de parking (parkingSpot) et vérifier son existence
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        assertNotNull(parkingSpot);
        // Etape 4 : vérifier que l'état de la colonne AVAILABLE = FALSE (place n'est plus disponible)
        assertFalse(parkingSpot.isAvailable());
        // Etape 5 : vérifier que la place qui était disponible soit bien celle retournée
        assertEquals(next, parkingSpot.getId());
    }

    @Test
    public void testParkingLotExit() {
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtilCar, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
    }

    @Test
    public void testParkingLotExitACar() throws InterruptedException {
        // DP : On relance le test précédent
        testParkingACar();

        // Initialisation du service parking
        ParkingService parkingService = new ParkingService(inputReaderUtilCar,
                parkingSpotDAO, ticketDAO);
        // On attend 1 seconde avant de sortir le véhicule
        Thread.sleep(1000);
        // Récupération du véhicule pour la sortie
        parkingService.processExitingVehicle();
        // Etape 1 : récupérer le ticket du véhicule immatriculé "ABCDEF" ==>mock
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        // Etape 2 : vérification de l'existence du ticket
        assertNotNull(ticket);
        assertEquals(1,ticket.getId());
        // Etape 3 : vérification que le ticket a une date de sortie du véhicule
        assertNotNull(ticket.getOutTime());
        // Etape 4 : vérification que le prix du ticket est correct
        assertEquals(0, ticket.getPrice());

    }
}
