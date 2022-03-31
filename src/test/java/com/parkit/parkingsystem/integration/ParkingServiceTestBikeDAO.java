//package com.parkit.parkingsystem.integration;
//
//import com.parkit.parkingsystem.constants.ParkingType;
//import com.parkit.parkingsystem.dao.ParkingSpotDAO;
//import com.parkit.parkingsystem.dao.TicketDAO;
//import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
//import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
//import com.parkit.parkingsystem.model.ParkingSpot;
//import com.parkit.parkingsystem.model.Ticket;
//import com.parkit.parkingsystem.service.ParkingService;
//import com.parkit.parkingsystem.util.InputReaderUtil;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class ParkingServiceTestBikeDAO {
//
//    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
//    private static ParkingSpotDAO parkingSpotDAO;
//    private static TicketDAO ticketDAO;
//    private static DataBasePrepareService dataBasePrepareService;
//
//    @Mock
//    private static InputReaderUtil inputReaderUtilBike;
//
//    @BeforeAll
//    private static void setUp() throws Exception {
//        parkingSpotDAO = new ParkingSpotDAO();
//        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
//        ticketDAO = new TicketDAO();
//        ticketDAO.dataBaseConfig = dataBaseTestConfig;
//        dataBasePrepareService = new DataBasePrepareService();
//    }
//
//    @BeforeEach
//    private void setUpPerTest() throws Exception {
//        when(inputReaderUtilBike.readSelection()).thenReturn(2);
//        when(inputReaderUtilBike.readVehicleRegistrationNumber()).thenReturn("Bike01");
//
//    }
//
//    @AfterEach
//    void cleanUp() {dataBasePrepareService.clearDataBaseEntries();}
//
//    @Test
//    public void testParkingABike() {
//        ParkingService parkingService = new ParkingService(inputReaderUtilBike, parkingSpotDAO, ticketDAO);
//        // DP : récuperer le no de la prochaine classe disponible pour une voiture
//        int next = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
//        parkingService.processIncomingVehicle();
//        // Etape 1 : récupérer le ticket du véhicule immatriculé "ABCDEF" ==>mock
//        Ticket ticket = ticketDAO.getTicket("Bike01");
//        // Etape 2 : vérifier l'existence du ticket
//        assertNotNull(ticket);
//        // Etape 3 : récupérer l'ID de parking (parkingSpot) et vérifier son existence
//        ParkingSpot parkingSpot = ticket.getParkingSpot();
//        assertNotNull(parkingSpot);
//        // Etape 4 : vérifier que l'état de la colonne AVAILABLE = FALSE (place n'est plus disponible)
//        assertFalse(parkingSpot.isAvailable());
//        // Etape 5 : vérifier que la place qui était disponible soit bien celle retournée
//        assertEquals(next, parkingSpot.getId());
//        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
//    }
//
//    @Test
//    public void testParkingLotExit() {
//        testParkingABike();
//        ParkingService parkingService = new ParkingService(inputReaderUtilBike, parkingSpotDAO, ticketDAO);
//        parkingService.processExitingVehicle();
//    }
//
//    @Test
//    public void testParkingLotExitABIke()throws InterruptedException {
//        // DP : On relance le test précédent
//        testParkingABike();
//        // Initialisation du service parking
//        ParkingService parkingService = new ParkingService(inputReaderUtilBike,
//                parkingSpotDAO, ticketDAO);
//        // On attend 1 seconde avant de sortir le véhicule
//        Thread.sleep(1000);
//        // Récupération du véhicule pour la sortie
//        parkingService.processExitingVehicle();
//        // Etape 1 : récupérer le ticket du véhicule immatriculé "ABCDEF" ==>mock
//        Ticket ticket = ticketDAO.getTicket("Bike01");
//        // Etape 2 : vérification de l'existence du ticket
//        Assertions.assertNotNull(ticket);
//        // Etape 3 : vérification que le ticket a une date de sortie du véhicule
//        Assertions.assertNotNull(ticket.getOutTime());
//        // Etape 4 : vérification que le prix du ticket est correct
//        // On teste la valeur 0, car dans le test on rentre et on sort tout de suite.
//        Assertions.assertEquals(0, ticket.getPrice());
//    }
//
//}