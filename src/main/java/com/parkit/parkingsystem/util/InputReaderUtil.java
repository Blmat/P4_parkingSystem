package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputReaderUtil {


    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    public int readSelection() {
        Scanner scan = new Scanner(System.in);
        try {
            return Integer.parseInt(scan.nextLine());
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    public String readVehicleRegistrationNumber() throws Exception {
        Scanner scan= new Scanner(System.in);
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            } else if (vehicleRegNumber.trim().length() > 10) {
                throw new IllegalArgumentException("The input is too long. it must be less that 11 digits");
            }
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            scan.close();
            throw e;
        }
    }


}
