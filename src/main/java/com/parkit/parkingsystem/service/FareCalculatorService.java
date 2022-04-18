package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null)
                || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        //calculate duration
        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();
        double duration = (outHour - inHour) / (1000 * 60 * 60); //60*60*1000 =1h
        //if duration is less than 30 min set price to 0
        if (duration < 0.5) {
            ticket.setPrice(0);
            return;
        }
        //initialize reduction to 1 when multiply nothing will happen
        double reduction = 1;
        //check in the ticket if user can get reduction
        if (ticket.isDiscount()) {
            reduction = Fare.DISCOUNT_FOR_REGULAR_CUSTOMER;
        }
        //change the fare depending on the vehicle type
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR:
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * reduction);
                break;

            case BIKE:
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * reduction);
                break;

            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}