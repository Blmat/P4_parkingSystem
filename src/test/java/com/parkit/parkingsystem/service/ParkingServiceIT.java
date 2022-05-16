package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceIT {

    private static ParkingService parkingService;
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();


    @Mock
    private InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;

    }

//    @Test
//    @DisplayName("Vehicle Already Enter")
//    public void VehicleAlreadyEnterTest() throws Exception {
//        when(inputReaderUtil.readSelection()).thenReturn(1);
//        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFH");
//        dataBasePrepareService.clearDataBaseEntries();
//        ParkingService parkingServiceIn1 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//        parkingServiceIn1.processIncomingVehicle();
//
//        ParkingService parkingServiceIn2 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
//        parkingServiceIn2.processIncomingVehicle();
//
//        Ticket ticket2 = ticketDAO.getTicket("ABCDEFH");
//
//        boolean allowEnter = ParkingService.vehiculeAlreadyEnter(ticket2);
//
//        assertThat(allowEnter).isEqualTo(true);
//
//    }
//
//    @Test
//    @DisplayName("Vehicule Is Allow Enter")
//    public void vehiculeIsEnterTest() throws ParseException {
//        Ticket ticket1 = new Ticket();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        Date outTime = sdf.parse("2020/03/24 10:30");
//
//        ticket1.setVehicleRegNumber("AZERTY");
//        ticket1.setOutTime(outTime);
//
//        Ticket ticket2 = new Ticket();
//
//        ticket2.setVehicleRegNumber("AZERTY");
//
//        boolean allowEnter = ParkingService.vehiculeAlreadyEnter(ticket2);
//
//        assertThat(allowEnter).isEqualTo(true);
//    }










}
